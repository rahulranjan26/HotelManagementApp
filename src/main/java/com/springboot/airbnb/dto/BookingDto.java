package com.springboot.airbnb.dto;

import com.springboot.airbnb.entity.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {

    private Long bookingId;
    private Integer roomsCount;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private BookingStatus status;
    private Set<GuestDto> guests;

}
