package com.springboot.airbnb.controller;



import com.springboot.airbnb.dto.HotelDto;
import com.springboot.airbnb.dto.HotelSearchRequest;
import com.springboot.airbnb.entity.Hotel;
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
    public ResponseEntity<Page<HotelDto>> searchHotels(@RequestBody HotelSearchRequest hotelSearchRequest) {
        log.info("Searching hotels");
        Page<HotelDto> hotel = inventoryService.searchHotels(hotelSearchRequest);
        return ResponseEntity.ok(hotel);
    }
}
