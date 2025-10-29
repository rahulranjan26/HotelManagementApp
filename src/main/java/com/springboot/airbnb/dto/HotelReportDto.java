package com.springboot.airbnb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelReportDto {
    private Long hotelId;
    private BigDecimal totalRevenue;
    private BigDecimal averageRevenuePerDay;
    private int totalBookings;
}
