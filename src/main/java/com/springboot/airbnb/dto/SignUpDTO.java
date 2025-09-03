package com.springboot.airbnb.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SignUpDTO {
    private String email;
    private String password;
    private String name;
}
