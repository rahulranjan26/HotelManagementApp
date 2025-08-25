package com.springboot.airbnb.service;

import com.springboot.airbnb.dto.BookingDto;
import com.springboot.airbnb.dto.BookingRequest;
import com.springboot.airbnb.dto.GuestDto;

import java.util.List;

public interface BookingService {
    BookingDto openABooking(BookingRequest bookingRequest);

    BookingDto addTheGuestToTheBooking(Long bookingId, List<GuestDto> guests);
}
