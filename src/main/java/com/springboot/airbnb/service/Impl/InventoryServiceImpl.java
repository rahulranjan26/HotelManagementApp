package com.springboot.airbnb.service.Impl;


import com.springboot.airbnb.entity.Inventory;
import com.springboot.airbnb.entity.Room;
import com.springboot.airbnb.repository.HotelRepository;
import com.springboot.airbnb.repository.InventoryRepository;
import com.springboot.airbnb.repository.RoomRepository;
import com.springboot.airbnb.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;


    @Override
    public void initializeInventoryForAYear(Room room) {
        LocalDateTime startDate = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endDate = startDate.plusYears(1).minusDays(1);
        LocalDateTime currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            boolean exists = inventoryRepository.existsByHotelAndRoomAndDate(room.getHotel(), room, currentDate.truncatedTo(ChronoUnit.DAYS));
            if (exists) {
                log.info("Skipping existing inventory for date:  {}", currentDate);
                currentDate = currentDate.plusDays(1);
                continue;
            }
            Inventory inventory = Inventory.builder()
                    .hotel(room.getHotel())
                    .bookedCount(0)
                    .city(room.getHotel().getCity())
                    .price(room.getBasePrice())
                    .totalCount(room.getTotalCount())
                    .surgeFactor(BigDecimal.valueOf(1L))
                    .date(currentDate)
                    .closed(false)
                    .room(room)
                    .build();
            inventoryRepository.save(inventory);

        }
    }

    @Override
    public void deleteInventory(Room room) {
        LocalDateTime today = LocalDateTime.now();
        inventoryRepository.deleteInventoryByDateAfterAndRoom(today, room);
    }
}
