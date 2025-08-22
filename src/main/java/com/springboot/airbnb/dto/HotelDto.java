package com.springboot.airbnb.dto;

import com.springboot.airbnb.entity.Amnety;
import com.springboot.airbnb.entity.HotelContactInfo;
import com.springboot.airbnb.entity.Photo;
import com.springboot.airbnb.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/***
 * DTO for {@link com.springboot.airbnb.entity.Hotel}
 ***/

@AllArgsConstructor
@Getter
@ToString
@Setter
public class HotelDto implements Serializable {
    private final Long hotelId;
    private final String name;
    private final String city;
    private final List<Photo> photos;
    private final List<Amnety> amenities;
    private final HotelContactInfo hotelContactInfo;
    private final Boolean isActive;

}