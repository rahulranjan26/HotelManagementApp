package com.springboot.airbnb.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class HotelSearchRequest {

    private String city;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer roomsCount;

    private Integer page = 0;
    private Integer size = 10;


}
