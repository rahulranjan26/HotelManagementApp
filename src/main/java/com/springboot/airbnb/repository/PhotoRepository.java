package com.springboot.airbnb.repository;

import com.springboot.airbnb.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
