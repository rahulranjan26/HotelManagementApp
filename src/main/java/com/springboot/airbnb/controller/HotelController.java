package com.springboot.airbnb.controller;

import com.springboot.airbnb.dto.HotelDto;
import com.springboot.airbnb.service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/admin/hotels")
@RequiredArgsConstructor
@Slf4j
public class HotelController {

    private final HotelService hotelService;

    @PostMapping()
    public ResponseEntity<HotelDto> createNewHotels(@RequestBody HotelDto hotelDto) {
        log.info("Inside teh controller to get create the hitel with name :{}", hotelDto.getName());
        HotelDto newHotel = hotelService.createNewHotel(hotelDto);
        return new ResponseEntity<>(newHotel, HttpStatus.CREATED);
    }

    @GetMapping(path = "/{hotelId}")
    public ResponseEntity<HotelDto> getHotelById(@PathVariable Long hotelId) {
        log.info("Finding hotel with hotelId:{}", hotelId);
        return new ResponseEntity<>(hotelService.getHotelById(hotelId), HttpStatus.OK);
    }

}
