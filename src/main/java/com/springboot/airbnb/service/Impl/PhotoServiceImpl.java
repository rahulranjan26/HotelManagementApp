package com.springboot.airbnb.service.Impl;

import com.springboot.airbnb.entity.Hotel;
import com.springboot.airbnb.entity.Photo;
import com.springboot.airbnb.entity.Room;
import com.springboot.airbnb.repository.PhotoRepository;
import com.springboot.airbnb.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;


    @Override
    public Photo createNewPhotoForRoom(Room room, Photo photo) {
        Photo newPhoto = Photo.builder()
                .room(room)
                .photoName(photo.getPhotoName())
                .build();

        return photoRepository.save(newPhoto);
    }

    @Override
    public Photo getPhotoOfRoomById(Long photoId) {
        return photoRepository.findById(photoId).orElse(null);
    }

    @Override
    public Boolean deletePhotoOfRoomById(Long photoId) {
        photoRepository.deleteById(photoId);
        return true;
    }

    @Override
    public Photo createNewPhotoForHotel(Hotel hotel, Photo photo) {
        Photo newPhoto = Photo.builder()
                .hotel(hotel)
                .photoName(photo.getPhotoName())
                .build();

        return photoRepository.save(newPhoto);
    }

    @Override
    public Photo getPhotoOfHotelById(Long photoId) {
        return photoRepository.findById(photoId).orElse(null);
    }

    @Override
    public Boolean deletePhotoOfHotelById(Long photoId) {
        photoRepository.deleteById(photoId);
        return true;

    }
}
