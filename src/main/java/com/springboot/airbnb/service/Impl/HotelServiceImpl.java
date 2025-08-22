package com.springboot.airbnb.service.Impl;

import com.springboot.airbnb.dto.HotelDto;
import com.springboot.airbnb.entity.Amnety;
import com.springboot.airbnb.entity.Hotel;
import com.springboot.airbnb.entity.Photo;
import com.springboot.airbnb.repository.AmnetyRepository;
import com.springboot.airbnb.repository.HotelRepository;
import com.springboot.airbnb.repository.PhotoRepository;
import com.springboot.airbnb.service.HotelService;
import com.springboot.airbnb.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    public final ModelMapper modelMapper;


    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("Creating a new hotel with name:{}", hotelDto.getName());
        Hotel hotelToBeSaved = modelMapper.map(hotelDto, Hotel.class);
        hotelToBeSaved.setIsActive(false);


        // Convert photos DTO -> entity
        List<Photo> photos = hotelDto.getPhotos().stream()
                .map(p -> {
                    Photo photo = new Photo();
                    photo.setPhotoName(p.getPhotoName());
                    photo.setHotel(hotelToBeSaved); // back reference
                    return photo;
                })
                .toList();

        // Convert amenities DTO -> entity
        List<Amnety> amenities = hotelDto.getAmenities().stream()
                .map(a -> {
                    Amnety amnety = new Amnety();
                    amnety.setAmnetyName(a.getAmnetyName());
                    amnety.setHotel(hotelToBeSaved); // back reference
                    return amnety;
                })
                .toList();

        // Attach children to parent
        hotelToBeSaved.setPhotos(photos);
        hotelToBeSaved.setAmenities(amenities);

        // Persist parent (cascades children)
        Hotel savedHotel = hotelRepository.save(hotelToBeSaved);
        log.info("Hotel saved with new hotel Id:{}", savedHotel.getHotelId());
        return modelMapper.map(savedHotel, HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long hotelId) {
        log.info("Getting hotel with id:{}", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id:" + hotelId));
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    @Transactional
    public HotelDto updateHotelById(Long hotelId, HotelDto hotelDto) {
        log.info("Updating the hotel with hotel Id:{}", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("the hotel was not found with hotel id: " + hotelId));
//        List<Photo> photos = hotelDto.getPhotos();
//        List<Amnety> amneties = hotelDto.getAmenities();
        hotel.getPhotos().clear();
        for (Photo photoDto : hotelDto.getPhotos()) {
            Photo photo = new Photo();
            photo.setPhotoName(photoDto.getPhotoName());
            photo.setHotel(hotel); // set back-reference
            hotel.getPhotos().add(photo);
        }

        hotel.getAmenities().clear();
        for (Amnety amnetyDto : hotelDto.getAmenities()) {
            Amnety amnety = new Amnety();
            amnety.setAmnetyName(amnetyDto.getAmnetyName());
            amnety.setHotel(hotel); // set back-reference
            hotel.getAmenities().add(amnety);
        }

        hotel.setIsActive(false);
        hotel.setCity(hotelDto.getCity());
        hotel.setName(hotelDto.getName());
        hotel.getHotelContactInfo().setAddress(hotelDto.getHotelContactInfo().getAddress());
        hotel.getHotelContactInfo().setPhoneNumber(hotelDto.getHotelContactInfo().getPhoneNumber());
        hotel.getHotelContactInfo().setEmail(hotelDto.getHotelContactInfo().getEmail());
        hotel.getHotelContactInfo().setLocation(hotelDto.getHotelContactInfo().getLocation());
        Hotel updated = hotelRepository.save(hotel);

        return modelMapper.map(updated, HotelDto.class);
    }


    @Override
    public Boolean deleteHotelById(Long hotelId) {
        log.info("Deleting the hotel with hotel Id:{}", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("the hotel was not found with hotel id: " + hotelId));
        hotelRepository.deleteById(hotelId);
        return true;

        // Delete the future inventories --> TODO
    }
}
