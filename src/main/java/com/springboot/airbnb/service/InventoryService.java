package com.springboot.airbnb.service;

import com.springboot.airbnb.dto.HotelInfoDto;
import com.springboot.airbnb.dto.HotelPriceDto;
import com.springboot.airbnb.dto.HotelSearchRequest;
import com.springboot.airbnb.entity.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {

    void initializeInventoryForAYear(Room room);

    void deleteInventory(Room room);

    Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest);

    HotelInfoDto getHotelInfo(Long hotelId);

}
