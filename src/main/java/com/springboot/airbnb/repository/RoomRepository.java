package com.springboot.airbnb.repository;

import com.springboot.airbnb.entity.Hotel;
import com.springboot.airbnb.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHotel(Hotel hotel);
}