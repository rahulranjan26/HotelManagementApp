package com.springboot.airbnb.dto;

import com.springboot.airbnb.entity.enums.Role;
import lombok.*;

import java.util.Set;



@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto  {
    private  Long userId;
    private  String email;
    private  String name;
    private  Set<Role> roles;
}