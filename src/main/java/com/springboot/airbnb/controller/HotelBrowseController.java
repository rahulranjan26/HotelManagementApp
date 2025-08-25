package com.springboot.airbnb.controller;


import com.springboot.airbnb.dto.HotelInfoDto;
import com.springboot.airbnb.dto.HotelPriceDto;
import com.springboot.airbnb.dto.HotelSearchRequest;
import com.springboot.airbnb.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path="/hotels")
public class HotelBrowseController {

    private final InventoryService inventoryService;


    @PostMapping(path="/search")
    public ResponseEntity<Page<HotelPriceDto>> searchHotels(@RequestBody HotelSearchRequest hotelSearchRequest) {
        log.info("Searching hotels");
        var hotel = inventoryService.searchHotels(hotelSearchRequest);
        return ResponseEntity.ok(hotel);
    }

    @GetMapping(path="/{hotelId}/info")
    public ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long hotelId) {
        log.info("Getting hotel info for hotelId: {}", hotelId);
        HotelInfoDto hotelInfo = inventoryService.getHotelInfo(hotelId);
        return ResponseEntity.ok(hotelInfo);
    }

}
