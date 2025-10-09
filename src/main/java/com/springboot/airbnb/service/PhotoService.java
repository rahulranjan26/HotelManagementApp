package com.springboot.airbnb.service;

import com.springboot.airbnb.entity.Hotel;
import com.springboot.airbnb.entity.Photo;
import com.springboot.airbnb.entity.Room;

public interface PhotoService {

    Photo createNewPhotoForRoom(Room room, Photo photo);
    Photo getPhotoOfRoomById(Long photoId);
    Boolean deletePhotoOfRoomById(Long photoId);

    Photo createNewPhotoForHotel(Hotel hotel, Photo photo);
    Photo getPhotoOfHotelById(Long photoId);
    Boolean deletePhotoOfHotelById(Long photoId);


}
