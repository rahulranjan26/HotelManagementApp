package com.springboot.airbnb.repository;

import com.springboot.airbnb.entity.Hotel;
import com.springboot.airbnb.entity.Inventory;
import com.springboot.airbnb.entity.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    void deleteInventoryByDateAfterAndRoom(LocalDateTime dateAfter, Room room);

    boolean existsByHotelAndRoomAndDate(Hotel hotel, Room room, LocalDateTime date);


    @Query("""
            
                select i.hotel from Inventory i
                                    where i.city = :city
                                        and i.date between :startDate and :endDate
                                        and (i.totalCount - i.bookedCount) >= :roomsCount
                                        and i.closed = false
                                    group by i.hotel
                                    having count(distinct i.date) = :dateCount
            """)
    Page<Hotel> findHotelWithAvailableRepository(
            @Param("city") String city,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("roomsCount") Integer roomsCount,
            @Param("dateCount") Integer dateCount,
            Pageable pageable
    );

}