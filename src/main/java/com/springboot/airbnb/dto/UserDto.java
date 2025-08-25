package com.springboot.airbnb.dto;

import com.springboot.airbnb.entity.enums.Role;
import lombok.*;

import java.io.Serializable;
import java.util.Set;



@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
    private  Long userId;
    private  String email;
    private  String password;
    private  String name;
    private  Set<Role> roles;
}