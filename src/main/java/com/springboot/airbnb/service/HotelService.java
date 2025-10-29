package com.springboot.airbnb.service;

import com.springboot.airbnb.dto.HotelDto;

import java.util.List;

public interface HotelService {

    HotelDto createNewHotel(HotelDto hotelDto);

    HotelDto getHotelById(Long hotelId);

    HotelDto updateHotelById(Long hotelId,HotelDto hotelDto);

    Boolean deleteHotelById(Long hotelId);

    HotelDto deletePhotoForHotelById(Long hotelId, Long photoId);

    HotelDto activateHotelById(Long hotelId);

    List<HotelDto> getAllHotels();
}
