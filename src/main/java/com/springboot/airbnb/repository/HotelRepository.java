package com.springboot.airbnb.repository;

import com.springboot.airbnb.entity.Hotel;
import com.springboot.airbnb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByOwner(User owner);
}