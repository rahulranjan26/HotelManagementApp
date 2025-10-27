package com.springboot.airbnb.service.Impl;

import com.springboot.airbnb.dto.BookingDto;
import com.springboot.airbnb.dto.BookingRequest;
import com.springboot.airbnb.dto.GuestDto;
import com.springboot.airbnb.entity.*;
import com.springboot.airbnb.entity.enums.BookingStatus;
import com.springboot.airbnb.exceptions.ResourceNotFoundException;
import com.springboot.airbnb.repository.*;
import com.springboot.airbnb.service.BookingService;
import com.springboot.airbnb.service.CheckOutService;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.param.RefundCreateParams;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final InventoryRepository inventoryRepository;
    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;
    private final GuestRepository guestRepository;
    private final CheckOutService checkOutService;

    @Value("${frontend.url}")
    private String url;

    @Override
    @Transactional
    public BookingDto openABooking(BookingRequest bookingRequest) {

        log.info("We are booking rooms for hotel with hotel id : {}", bookingRequest.getHotelId());
//        System.out.println(hotelRepository.findAll());
        Hotel hotel = hotelRepository.findById(bookingRequest.getHotelId()).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with Id : " + bookingRequest.getHotelId()));
        Room room = roomRepository.findById(bookingRequest.getRoomId()).orElseThrow(() -> new ResourceNotFoundException("Room with id : " + bookingRequest.getRoomId()));
        long numberOfDays = ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate()) + 1;

        List<Inventory> inventories = inventoryRepository.findTheBookingRangeForGivenDates(
                bookingRequest.getHotelId(),
                bookingRequest.getRoomId(),
                bookingRequest.getCheckInDate(),
                bookingRequest.getCheckOutDate(),
                bookingRequest.getRoomsCount()
        );
        log.info("Size: {}", inventories.size());
        log.info("Size: {}", numberOfDays);

        if (inventories.size() != numberOfDays) {
            throw new IllegalArgumentException("The rooms are not available for the mentioned dates");
        }

        for (Inventory inventory : inventories) {
            inventory.setReservedCount(inventory.getReservedCount() + bookingRequest.getRoomsCount());
        }
        inventoryRepository.saveAll(inventories);

//        User user = new User();
//        user.setUserId(1L);

        Booking booking = Booking.builder()
                .amount(BigDecimal.valueOf(100))
                .checkInDate(bookingRequest.getCheckInDate())
                .checkOutDate(bookingRequest.getCheckOutDate())
                .user(getCurrentUser())
                .status(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .roomsCount(bookingRequest.getRoomsCount())
                .build();
        return modelMapper.map(bookingRepository.save(booking), BookingDto.class);
    }

    @Override
    @Transactional
    public BookingDto addTheGuestToTheBooking(Long bookingId, List<GuestDto> guests) {
        log.info("We are adding guest to the booking with bookingId : {}", bookingId);
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("The bookingId was not found : " + bookingId));

        if (!booking.getUser().equals(getCurrentUser())) {
            throw new IllegalArgumentException("The user is not same");
        }

        if (!booking.getStatus().equals(BookingStatus.RESERVED)) {
            throw new IllegalArgumentException("The rooms are not reserved for the mentioned dates");
        }

        if (booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("The ticket has expired. PLease book new ticket");

        }
        for (GuestDto guestDto : guests) {
            Guest newGuest = Guest.builder()
                    .name(guestDto.getName())
                    .gender(guestDto.getGender())
                    .age(guestDto.getAge())
                    .user(getCurrentUser())
                    .build();
            Guest savedUser = guestRepository.save(newGuest);
            booking.getGuests().add(savedUser);
        }
        booking.setStatus(BookingStatus.GUEST_ADDED);
        return modelMapper.map(bookingRepository.save(booking), BookingDto.class);
    }

    @Override
    @Transactional
    public String initiatePayment(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("The booking Id is not found: " + bookingId));

        if (!booking.getUser().equals(getCurrentUser())) {
            throw new IllegalArgumentException("The user is not same");
        }
        if (booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("The ticket has expired. PLease book new ticket");
        }

        String sessionUrl = checkOutService.getCheckOutSession(booking, url + "/payments/success", url + "/payments/failure");

        booking.setStatus(BookingStatus.PAYMENT_PENDING);
        bookingRepository.save(booking);
        return sessionUrl;
    }

    @Override
    public void capturePayment(Event event) {
        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
            if (session != null) {
                String sessionId = session.getId();
                Booking booking = bookingRepository.findByPaymentSessionId(sessionId).orElseThrow(
                        () -> new ResourceNotFoundException("The Booking was not found " + sessionId)
                );
                booking.setStatus(BookingStatus.CONFIRMED);
                bookingRepository.save(booking);

                inventoryRepository.findAndLockReservedInventory(
                        booking.getRoom().getRoomId(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate(),
                        booking.getRoomsCount());

                inventoryRepository.confirmBooking(
                        booking.getRoom().getRoomId(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate(),
                        booking.getRoomsCount());

                log.info("Booking confirmed for session Id : {}", sessionId);

            }
        } else {
            log.warn("Unhandled event type : {}", event.getType());
        }
    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("The booking Id is not found: " + bookingId));

        if (!booking.getUser().equals(getCurrentUser())) {
            throw new IllegalArgumentException("The user is not same");
        }

        if (!booking.getStatus().equals(BookingStatus.CONFIRMED))
            throw new ResourceNotFoundException("The booking is not yet confirmed.");

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        inventoryRepository.findAndLockReservedInventory(
                booking.getRoom().getRoomId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getRoomsCount());

        inventoryRepository.cancelBooking(
                booking.getRoom().getRoomId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getRoomsCount());
        log.info("Booking cancelled for booking Id : {}", booking.getBookingId());

        try {
            Session session = Session.retrieve(booking.getPaymentSessionId());
            RefundCreateParams refundParams = RefundCreateParams.builder()
                    .setPaymentIntent(session.getPaymentIntent())
                    .build();

            Refund.create(refundParams);

        } catch (StripeException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public String getBookingStatus(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("The booking Id is not found: " + bookingId));

        if (!booking.getUser().equals(getCurrentUser())) {
            throw new IllegalArgumentException("The user is not same");
        }
        return booking.getStatus().name();
    }

    public static User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
