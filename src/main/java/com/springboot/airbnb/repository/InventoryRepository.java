package com.springboot.airbnb.repository;

import com.springboot.airbnb.entity.Hotel;
import com.springboot.airbnb.entity.Inventory;
import com.springboot.airbnb.entity.Room;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    void deleteInventoryByDateAfterAndRoom(LocalDateTime dateAfter, Room room);

    boolean existsByHotelAndRoomAndDate(Hotel hotel, Room room, LocalDateTime date);


    @Query("""
            
                select i.hotel from Inventory i
                                    where i.city = :city
                                        and i.date between :startDate and :endDate
                                        and (i.totalCount - i.bookedCount - i.reservedCount) >= :roomsCount
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

    @Query("""
            select i from Inventory i
            where i.room.roomId = :roomId
            and i.date between :checkInDate and :checkOutDate
            and (i.totalCount - i.bookedCount -i.reservedCount) >= :roomsCount
                        and i.closed = false
            """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Inventory> findTheBookingRangeForGivenDates(
            @Param("hotelId") Long hotelId,
            @Param("roomId") Long roomId,
            @Param("checkInDate") LocalDateTime checkInDate,
            @Param("checkOutDate") LocalDateTime checkOutDate,
            @Param("roomsCount") Integer roomsCount);


    @Query("""
                            select i from Inventory i
                            where i.room.roomId =:roomId
                            and i.date between :startDate and :endDate
                            and (i.totalCount - i.bookedCount ) >= :roomsCount
                            and i.closed = false
            
            """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Inventory> findAndLockReservedInventory(
            @Param("roomId") Long roomId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("roomsCount") Integer roomsCount
    );


    @Modifying
    @Query("""
            update Inventory i
            set i.reservedCount = i.reservedCount - :roomsCount,
            i.bookedCount = i.bookedCount+:roomsCount
            where i.room.roomId=:roomId
            and i.date between :checkInDate and :checkOutDate
            and (i.totalCount-i.bookedCount) >=:roomsCount
            and i.reservedCount >= :roomsCount
            and i.closed=false
            
            """)
    void confirmBooking(
            @Param("roomId") Long roomId,
            @Param("checkInDate") LocalDateTime checkInDate,
            @Param("checkOutDate") LocalDateTime checkOutDate,
            @Param("roomsCount") Integer roomsCount);

    @Modifying
    @Query("""
            update Inventory i
            set i.bookedCount = i.bookedCount-:roomsCount
            where i.room.roomId=:roomId
            and i.date between :checkInDate and :checkOutDate
            and (i.totalCount-i.bookedCount) >= :roomsCount
            and i.closed=false
            
            """)
    void cancelBooking(
            @Param("roomId") Long roomId,
            @Param("checkInDate") LocalDateTime checkInDate,
            @Param("checkOutDate") LocalDateTime checkOutDate,
            @Param("roomsCount") Integer roomsCount);


    List<Inventory> findByHotelAndDateBetween(Hotel hotel, LocalDateTime startDate, LocalDateTime endDate);
}