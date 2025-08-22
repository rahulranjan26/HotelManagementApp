package com.springboot.airbnb.entity;

import com.springboot.airbnb.entity.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;


    @Column(nullable = false, unique = true)
    private String email;


    @Column(nullable = false)
    private String password;

    private String name;


    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;



}
