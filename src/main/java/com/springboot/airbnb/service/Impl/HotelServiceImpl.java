package com.springboot.airbnb.service.Impl;

import com.springboot.airbnb.dto.HotelDto;
import com.springboot.airbnb.entity.Amnety;
import com.springboot.airbnb.entity.Hotel;
import com.springboot.airbnb.entity.Photo;
import com.springboot.airbnb.entity.User;
import com.springboot.airbnb.exceptions.ResourceNotFoundException;
import com.springboot.airbnb.repository.HotelRepository;
import com.springboot.airbnb.service.HotelService;
import com.springboot.airbnb.service.InventoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    public final ModelMapper modelMapper;
    public final InventoryService inventoryService;


    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("Creating a new hotel with name:{}", hotelDto.getName());
        Hotel hotelToBeSaved = modelMapper.map(hotelDto, Hotel.class);
        hotelToBeSaved.setIsActive(false);


        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        hotelToBeSaved.setOwner(user);

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
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.equals(hotel.getOwner())) {
            throw new IllegalArgumentException("The hotel is not owned by the current user.");
        }
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    @Transactional
    public HotelDto updateHotelById(Long hotelId, HotelDto hotelDto) {
        log.info("Updating the hotel with hotel Id:{}", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("the hotel was not found with hotel id: " + hotelId));
//        List<Photo> photos = hotelDto.getPhotos();
//        List<Amnety> amneties = hotelDto.getAmenities();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.equals(hotel.getOwner())) {
            throw new IllegalArgumentException("The hotel is not owned by the current user.");
        }
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
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.equals(hotel.getOwner())) {
            throw new IllegalArgumentException("The hotel is not owned by the current user.");
        }
        for (var room : hotel.getRooms()) {
            inventoryService.deleteInventory(room);
        }
        hotelRepository.deleteById(hotelId);
        return true;
    }

    @Override
    public HotelDto deletePhotoForHotelById(Long hotelId, Long photoId) {
        log.info("Deleting the photo with with photoId {} in hotel with hotelId {}", photoId, hotelId);
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel with hotelid not found: " + hotelId));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.equals(hotel.getOwner())) {
            throw new IllegalArgumentException("The hotel is not owned by the current user.");
        }
        List<Photo> photos = hotel.getPhotos();
        for (int i = 0; i < photos.size(); i++) {
            Photo photo = photos.get(i);
            if (photo.getPhotoId().equals(photoId)) {
                photos.remove(photo);
                break;
            }
        }
        Hotel savedHotel = hotelRepository.save(hotel);

        return modelMapper.map(savedHotel, HotelDto.class);

    }

    @Override
    public HotelDto activateHotelById(Long hotelId) {
        log.info("Activating hotel with hotelId {}", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel with hotelid not found: " + hotelId));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.equals(hotel.getOwner())) {
            throw new IllegalArgumentException("The hotel is not owned by the current user.");
        }
        hotel.setIsActive(true);
        Hotel savedHotel = hotelRepository.save(hotel);
        for (var room : hotel.getRooms()) {
            inventoryService.initializeInventoryForAYear(room);
        }
        return modelMapper.map(savedHotel, HotelDto.class);


    }
}
