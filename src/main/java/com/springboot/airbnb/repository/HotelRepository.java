package com.springboot.airbnb.repository;

import com.springboot.airbnb.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HotelRepository extends JpaRepository<Hotel, Long> {
}