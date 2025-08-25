package com.springboot.airbnb.service.Impl;


import com.springboot.airbnb.dto.*;
import com.springboot.airbnb.entity.*;
import com.springboot.airbnb.exceptions.ResourceNotFoundException;
import com.springboot.airbnb.repository.HotelMinPriceRepository;
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
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {
    private final ModelMapper modelMapper;
    private final InventoryRepository inventoryRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;


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
                    .reservedCount(0)
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
    public Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest) {
        log.info("Searching hotels with request: {}", hotelSearchRequest);
        Pageable pageable = PageRequest.of(hotelSearchRequest.getPage(), hotelSearchRequest.getSize());
        LocalDateTime truncatedStart = hotelSearchRequest.getStartDate();
        LocalDateTime truncatedEnd = hotelSearchRequest.getEndDate();
        int dateCount = (int) ChronoUnit.DAYS.between(truncatedStart, truncatedEnd) + 1;
        log.info("Final search parameters: city={}, startDate={}, endDate={}, roomsCount={}, dayCount={}",
                hotelSearchRequest.getCity(), truncatedStart, truncatedEnd,
                hotelSearchRequest.getRoomsCount(), dateCount);
        return hotelMinPriceRepository.findHotelsWithAvailableInventory(
                hotelSearchRequest.getCity(),
                truncatedStart,
                truncatedEnd,
                hotelSearchRequest.getRoomsCount(),
                dateCount,
                pageable
        );
    }

    @Override
    public HotelInfoDto getHotelInfo(Long hotelId) {
        log.info("Get hotel info with Id :{}", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("The hotel not found with id:" + hotelId));
        List<RoomDto> rooms = new ArrayList<>();
        for (var room : hotel.getRooms()) {
            RoomDto newRoomDto = RoomDto.builder()
                    .roomId(room.getRoomId())
                    .amenities(room.getAmenities().stream().map(el ->
                            Amnety.builder()
                                    .room(room)
                                    .amnetyName(el.getAmnetyName())
                                    .build()
                    ).toList())
                    .basePrice(room.getBasePrice())
                    .capacity(room.getCapacity())
                    .photos(room.getPhotos().stream().map((el) -> Photo.builder()
                            .room(room)
                            .photoName(el.getPhotoName())
                            .build()).toList())
                    .totalCount(room.getTotalCount())
                    .type(room.getType())
                    .build();
            rooms.add(newRoomDto);
        }
        HotelDto hotelDto = HotelDto.builder()
                .hotelContactInfo(hotel.getHotelContactInfo())
                .name(hotel.getName())
                .hotelId(hotel.getHotelId())
                .amenities(hotel.getAmenities())
                .photos(hotel.getPhotos())
                .city(hotel.getCity())
                .isActive(hotel.getIsActive())
                .build();
        return HotelInfoDto.builder().hotel(hotelDto).rooms(rooms).build();
    }


}
