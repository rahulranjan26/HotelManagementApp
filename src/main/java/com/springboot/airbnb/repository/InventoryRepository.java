package com.springboot.airbnb.repository;

import com.springboot.airbnb.entity.Hotel;
import com.springboot.airbnb.entity.Inventory;
import com.springboot.airbnb.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    void deleteInventoryByDateAfterAndRoom(LocalDateTime dateAfter, Room room);

    boolean existsByHotelAndRoomAndDate(Hotel hotel, Room room, LocalDateTime date);
}