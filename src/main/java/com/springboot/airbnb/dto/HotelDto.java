package com.springboot.airbnb.dto;

import com.springboot.airbnb.entity.Amnety;
import com.springboot.airbnb.entity.HotelContactInfo;
import com.springboot.airbnb.entity.Photo;
import com.springboot.airbnb.entity.Room;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/***
 * DTO for {@link com.springboot.airbnb.entity.Hotel}
 ***/


@Data
@ToString
public class HotelDto  {
    private  Long hotelId;
    private  String name;
    private  String city;
    private  List<Photo> photos;
    private  List<Amnety> amenities;
    private  HotelContactInfo hotelContactInfo;
    private  Boolean isActive;

}