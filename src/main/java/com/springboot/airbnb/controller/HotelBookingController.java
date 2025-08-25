package com.springboot.airbnb.controller;


import com.springboot.airbnb.dto.BookingDto;
import com.springboot.airbnb.dto.BookingRequest;
import com.springboot.airbnb.dto.GuestDto;
import com.springboot.airbnb.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/bookings")
@RequiredArgsConstructor
public class HotelBookingController {
    private final BookingService bookingService;
    @PostMapping(path="/init")
    public ResponseEntity<BookingDto> openABooking(@RequestBody BookingRequest bookingRequest){
        return ResponseEntity.ok(bookingService.openABooking(bookingRequest));
    }

    @PostMapping(path="/{bookingId}/addGuests")
    public ResponseEntity<BookingDto> addTheGuestToTheBooking(@PathVariable Long bookingId,
                                                              @RequestBody List<GuestDto> guests){
        return ResponseEntity.ok(bookingService.addTheGuestToTheBooking(bookingId,guests));
    }

}
