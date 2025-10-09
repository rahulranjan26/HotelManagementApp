package com.springboot.airbnb.dto;

import com.springboot.airbnb.entity.Amnety;
import com.springboot.airbnb.entity.Photo;
import com.springboot.airbnb.entity.Room;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for {@link Room}
 */
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto  {
    private  Long roomId;
    private  String type;
    private  BigDecimal basePrice;
    private  List<Photo> photos;
    private  List<Amnety> amenities;
    private  Integer totalCount;
    private  Integer capacity;
}
