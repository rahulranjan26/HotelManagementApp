package com.springboot.airbnb.service;

import com.springboot.airbnb.dto.RoomDto;

import java.util.List;

public interface RoomService {

    RoomDto createNewRoom(Long hotelId, RoomDto roomDto);

    List<RoomDto> getAllRoomInHotel(Long hotelId);

    RoomDto getRoomById(Long roomId);

    Boolean deleteRoomById(Long roomId);


    RoomDto updateRoomById(Long hotelId, Long roomId, RoomDto roomDto);
}
