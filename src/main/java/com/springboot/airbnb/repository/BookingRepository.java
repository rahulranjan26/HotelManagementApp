package com.springboot.airbnb.repository;

import com.springboot.airbnb.entity.Booking;
import com.springboot.airbnb.entity.Hotel;
import com.springboot.airbnb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface BookingRepository extends JpaRepository<Booking, Long> {



    Optional<Booking> findByPaymentSessionId(String paymentSessionId);

    List<Booking> findByHotel(Hotel hotel);

    List<Booking> findByHotelAndCreatedAtBetween(Hotel hotel, LocalDateTime createdAtAfter, LocalDateTime createdAtBefore);

    List<Booking> findByUser(User user);
}
