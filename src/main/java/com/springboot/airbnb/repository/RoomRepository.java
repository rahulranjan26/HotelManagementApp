package com.springboot.airbnb.repository;

import com.springboot.airbnb.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoomRepository extends JpaRepository<Room, Long> {
}