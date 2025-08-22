package com.springboot.airbnb.service.Impl;

import com.springboot.airbnb.dto.HotelDto;
import com.springboot.airbnb.entity.Amnety;
import com.springboot.airbnb.entity.Hotel;
import com.springboot.airbnb.entity.Photo;
import com.springboot.airbnb.repository.AmnetyRepository;
import com.springboot.airbnb.repository.HotelRepository;
import com.springboot.airbnb.repository.PhotoRepository;
import com.springboot.airbnb.service.HotelService;
import com.springboot.airbnb.service.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    public final ModelMapper modelMapper;
    private final PhotoRepository photoRepository;
    private final AmnetyRepository amnetyRepository;


    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("Creating a new hotel with name:{}", hotelDto.getName());
        Hotel hotelToBeSaved = modelMapper.map(hotelDto, Hotel.class);
        hotelToBeSaved.setIsActive(false);
        Hotel savedHotel = hotelRepository.save(hotelToBeSaved);
        for (var photos : hotelDto.getPhotos()) {
            Photo photo = new Photo();
            photo.setHotel(savedHotel);
            photo.setPhotoName(photos.getPhotoName());
            photoRepository.save(photo);
        }
        for (var amneties : hotelDto.getAmenities()) {
            Amnety amnety = new Amnety();
            amnety.setHotel(savedHotel);
            amnety.setAmnetyName(amneties.getAmnetyName());
            amnetyRepository.save(amnety);
        }
        log.info("Hotel saved with new hotel Id:{}", savedHotel.getHotelId());
        return modelMapper.map(savedHotel, HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long hotelId) {
        log.info("Getting hotel with id:{}", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id:" + hotelId));
        return modelMapper.map(hotel, HotelDto.class);
    }
}
