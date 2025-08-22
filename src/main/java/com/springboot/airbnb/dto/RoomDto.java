package com.springboot.airbnb.dto;

import com.springboot.airbnb.entity.Amnety;
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

/**
 * DTO for {@link Room}
 */
@AllArgsConstructor
@Getter
@ToString
@Setter
public class RoomDto implements Serializable {
    private final Long roomId;
    private final String type;
    private final BigDecimal basePrice;
    private final List<Photo> photos;
    private final List<Amnety> amenities;
    private final Integer totalCount;
    private final Integer capacity;
}
