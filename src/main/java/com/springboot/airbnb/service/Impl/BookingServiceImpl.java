package com.springboot.airbnb.service.Impl;

import com.springboot.airbnb.dto.BookingDto;
import com.springboot.airbnb.dto.BookingRequest;
import com.springboot.airbnb.dto.GuestDto;
import com.springboot.airbnb.entity.*;
import com.springboot.airbnb.entity.enums.BookingStatus;
import com.springboot.airbnb.exceptions.ResourceNotFoundException;
import com.springboot.airbnb.repository.*;
import com.springboot.airbnb.service.BookingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    private final GuestRepository guestRepostitory;

    @Override
    @Transactional
    public BookingDto openABooking(BookingRequest bookingRequest) {

        log.info("We are booking rooms for hotel with hotel id : {}", bookingRequest.getHotelId());
        Hotel hotel = hotelRepository.findById(bookingRequest.getHotelId()).orElseThrow(() -> new ResourceNotFoundException("Hotel with  not found with Id : " + bookingRequest.getHotelId()));
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

        User user = new User();
        user.setUserId(1L);

        Booking booking = Booking.builder()
                .amount(BigDecimal.valueOf(100))
                .checkInDate(bookingRequest.getCheckInDate())
                .checkOutDate(bookingRequest.getCheckOutDate())
                .user(getUserDummy())
                .status(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .payment(null)
                .roomsCount(bookingRequest.getRoomsCount())
                .build();
        return modelMapper.map(bookingRepository.save(booking), BookingDto.class);
    }

    @Override
    public BookingDto addTheGuestToTheBooking(Long bookingId, List<GuestDto> guests) {
        log.info("We are adding guest to the booking with bookingId : {}", bookingId);
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("The bookingId was not found : " + bookingId));

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
                    .user(getUserDummy())
                    .build();
            Guest savedUser = guestRepostitory.save(newGuest);
            booking.getGuests().add(savedUser);
        }
        booking.setStatus(BookingStatus.GUEST_ADDED);
        return modelMapper.map(bookingRepository.save(booking), BookingDto.class);
    }

    public static User getUserDummy() {
        User user = new User();
        user.setUserId(1L);
        return user;
    }
}
