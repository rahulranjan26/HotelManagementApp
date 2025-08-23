package com.springboot.airbnb.service.Impl;

import com.springboot.airbnb.dto.RoomDto;
import com.springboot.airbnb.entity.Amnety;
import com.springboot.airbnb.entity.Hotel;
import com.springboot.airbnb.entity.Photo;
import com.springboot.airbnb.entity.Room;
import com.springboot.airbnb.exceptions.ResourceNotFoundException;
import com.springboot.airbnb.repository.HotelRepository;
import com.springboot.airbnb.repository.PhotoRepository;
import com.springboot.airbnb.repository.RoomRepository;
import com.springboot.airbnb.service.AmnetyService;
import com.springboot.airbnb.service.PhotoService;
import com.springboot.airbnb.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {

    public final RoomRepository roomRepository;
    private final PhotoService photoService;
    private final AmnetyService amnetyService;
    private final ModelMapper modelMapper;
    private final HotelRepository hotelRepository;

    @Override
    public RoomDto createNewRoom(Long hotelId,RoomDto roomDto) {
        log.info("So we are creating a new room");
        Room newRoom = Room.builder()
                .type(roomDto.getType())
                .basePrice(roomDto.getBasePrice())
                .capacity(roomDto.getCapacity())
                .totalCount(roomDto.getTotalCount())
                .build();
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id:" + hotelId));
        newRoom.setHotel(hotel);
        Room savedRoom = roomRepository.save(newRoom);
        List<Photo> createdPhotos = new ArrayList<>();
        for (var pic: roomDto.getPhotos()) {
            Photo newPhoto = photoService.createNewPhotoForRoom(savedRoom, pic);
            createdPhotos.add(newPhoto);
        }

        List<Amnety> createdAmenities = new ArrayList<>();
        for (var amnety: roomDto.getAmenities()) {
            Amnety newAmnety = amnetyService.createNewAmnetyForRoom(savedRoom, amnety);
            createdAmenities.add(newAmnety);
        }
        return RoomDto.builder()
                .roomId(savedRoom.getRoomId())
                .type(savedRoom.getType())
                .basePrice(savedRoom.getBasePrice())
                .capacity(savedRoom.getCapacity())
                .totalCount(savedRoom.getTotalCount())
                .photos(createdPhotos)      // Use of actual created photos
                .amenities(createdAmenities) // Use of actual created amenities
                .build();
    }

    @Override
    public List<RoomDto> getAllRoomInHotel(Long hotelId) {
        log.info("So we are finding a all room");
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id:" + hotelId));
        List<Room> rooms = roomRepository.findByHotel(hotel);
        List<RoomDto> roomDtos = new ArrayList<>();
        for (var room: rooms) {
            RoomDto dto = RoomDto.builder()
                    .roomId(room.getRoomId())
                    .type(room.getType())
                    .basePrice(room.getBasePrice())
                    .capacity(room.getCapacity())
                    .totalCount(room.getTotalCount())
                    .photos(room.getPhotos())
                    .amenities(room.getAmenities())
                    .build();
            roomDtos.add(dto);
        }
        return roomDtos;
    }

    @Override
    public RoomDto getRoomById(Long roomId) {
        log.info("Room with Id: {} is being queries",roomId);
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room not found with id:" + roomId));
        RoomDto dto = RoomDto.builder()
                .roomId(room.getRoomId())
                .type(room.getType())
                .basePrice(room.getBasePrice())
                .capacity(room.getCapacity())
                .totalCount(room.getTotalCount())
                .photos(room.getPhotos())
                .amenities(room.getAmenities())
                .build();
        return dto;
    }

    @Override
    public Boolean deleteRoomById(Long roomId) {
        log.info("Room with Id: {} is being deleted",roomId);
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room not found with id:" + roomId));
        roomRepository.delete(room);
        return true;
    }



}
