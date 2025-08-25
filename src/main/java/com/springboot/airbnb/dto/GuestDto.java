package com.springboot.airbnb.dto;

import com.springboot.airbnb.entity.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GuestDto {
    private  Long guestId;
    private  UserDto user;
    private  String name;
    private  Gender gender;
    private  Integer age;
}