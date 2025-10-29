package com.springboot.airbnb.dto;

import com.springboot.airbnb.entity.enums.Gender;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProfileUpdateRequestDto {
    private LocalDateTime dateOfBirth;
    private Gender gender;
    private String name;
}
