package com.springboot.airbnb.controller;


import com.springboot.airbnb.dto.BookingDto;
import com.springboot.airbnb.dto.BookingRequest;
import com.springboot.airbnb.dto.GuestDto;
import com.springboot.airbnb.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class HotelBookingController {
    private final BookingService bookingService;

    @PostMapping(path = "/init")
    public ResponseEntity<BookingDto> openABooking(@RequestBody BookingRequest bookingRequest) {
        return ResponseEntity.ok(bookingService.openABooking(bookingRequest));
    }

    @PostMapping(path = "/{bookingId}/addGuests")
    public ResponseEntity<BookingDto> addTheGuestToTheBooking(@PathVariable Long bookingId,
                                                              @RequestBody List<GuestDto> guests) {
        return ResponseEntity.ok(bookingService.addTheGuestToTheBooking(bookingId, guests));
    }

    @PostMapping(path = "/{bookingId}/payments")
    public ResponseEntity<Map<String, String>> initiatePayment(@PathVariable Long bookingId) {
        String sessionUrl = bookingService.initiatePayment(bookingId);
        return ResponseEntity.ok(Map.of("SessionUrl", sessionUrl));
    }

    @PostMapping(path = "/{bookingId}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{bookingId}/status")
    public ResponseEntity<Map<String, String>> getBookingStatus(@PathVariable Long bookingId) {
        return ResponseEntity.ok(Map.of("BookingStatus", bookingService.getBookingStatus(bookingId)));
    }


}
