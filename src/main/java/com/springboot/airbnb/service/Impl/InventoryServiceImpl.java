package com.springboot.airbnb.service.Impl;


import com.springboot.airbnb.dto.HotelDto;
import com.springboot.airbnb.dto.HotelSearchRequest;
import com.springboot.airbnb.entity.Hotel;
import com.springboot.airbnb.entity.Inventory;
import com.springboot.airbnb.entity.Room;
import com.springboot.airbnb.repository.HotelRepository;
import com.springboot.airbnb.repository.InventoryRepository;
import com.springboot.airbnb.repository.RoomRepository;
import com.springboot.airbnb.service.InventoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {
    private final ModelMapper modelMapper;
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

    @Override
    public Page<HotelDto> searchHotels(HotelSearchRequest hotelSearchRequest) {
        log.info("Searching hotels with request: {}", hotelSearchRequest);
        Pageable pageable = PageRequest.of(hotelSearchRequest.getPage(), hotelSearchRequest.getSize());
        LocalDateTime truncatedStart = hotelSearchRequest.getStartDate();
        LocalDateTime truncatedEnd = hotelSearchRequest.getEndDate();
        int dateCount = (int) ChronoUnit.DAYS.between(truncatedStart, truncatedEnd) + 1;
        log.info("Final search parameters: city={}, startDate={}, endDate={}, roomsCount={}, dayCount={}",
                hotelSearchRequest.getCity(), truncatedStart, truncatedEnd,
                hotelSearchRequest.getRoomsCount(), dateCount);
        Page<Hotel> hotels = inventoryRepository.findHotelWithAvailableRepository(
                hotelSearchRequest.getCity(),
                truncatedStart,
                truncatedEnd,
                hotelSearchRequest.getRoomsCount(),
                dateCount,
                pageable
        );
        return hotels.map((element) -> modelMapper.map(element, HotelDto.class));
    }
}
