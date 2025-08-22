package com.springboot.airbnb.service;

import com.springboot.airbnb.dto.HotelDto;

public interface HotelService {

    HotelDto createNewHotel(HotelDto hotelDto);

    HotelDto getHotelById(Long hotelId);
}
