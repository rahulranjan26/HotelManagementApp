package com.springboot.airbnb.service;

import com.springboot.airbnb.dto.BookingDto;
import com.springboot.airbnb.dto.BookingRequest;
import com.springboot.airbnb.dto.GuestDto;
import com.springboot.airbnb.dto.HotelReportDto;
import com.stripe.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    BookingDto openABooking(BookingRequest bookingRequest);

    BookingDto addTheGuestToTheBooking(Long bookingId, List<GuestDto> guests);

    String initiatePayment(Long bookingId);

    void capturePayment(Event event);

    void cancelBooking(Long bookingId);

    String getBookingStatus(Long bookingId);

    List<BookingDto> getBookingsByHotelId(Long hotelId);

    HotelReportDto getHotelReport(Long hotelId, LocalDateTime startDate, LocalDateTime endDate);

    List<BookingDto> getMyBookings();
}
