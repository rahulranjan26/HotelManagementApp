package com.springboot.airbnb.controller;

import com.springboot.airbnb.dto.BookingDto;
import com.springboot.airbnb.dto.ProfileUpdateRequestDto;
import com.springboot.airbnb.service.BookingService;
import com.springboot.airbnb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final BookingService bookingService;

    @PatchMapping("/profile")
    public ResponseEntity<Void> updateUserProfile(@RequestBody ProfileUpdateRequestDto profileUpdateRequestDto) {
        return  ResponseEntity.ok(userService.updateUserProfile(profileUpdateRequestDto));
    }

    @GetMapping("/mybookings")
    public ResponseEntity<List<BookingDto>> getMyBookings(){
        return ResponseEntity.ok(bookingService.getMyBookings());
    }
    
}
