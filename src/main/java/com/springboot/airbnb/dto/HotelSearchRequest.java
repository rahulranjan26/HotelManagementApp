package com.springboot.airbnb.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelSearchRequest {

    private String city;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer roomsCount;

    private Integer page = 0;
    private Integer size = 10;


}
