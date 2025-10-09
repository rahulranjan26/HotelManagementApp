package com.springboot.airbnb.service;

import com.springboot.airbnb.entity.Amnety;
import com.springboot.airbnb.entity.Hotel;
import com.springboot.airbnb.entity.Room;


public interface AmnetyService {
    Amnety createNewAmnetyForRoom(Room room, Amnety amnety);

    Amnety getAmnetyOfRoomById(Room room, Long amnetyId);

    Boolean deleteAmnetyOfRoomById(Room room, Long amnetyId);

    Amnety createNewAmnetyForHotel(Hotel hotel, Amnety amnety);

    Amnety getAmnetyOfHotelById(Hotel hotel, Long amnetyId);

    Boolean deleteAmnetyOfHotelById(Hotel hotel, Long amnetyId);
}

