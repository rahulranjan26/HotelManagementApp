package com.springboot.airbnb.entity;


import com.springboot.airbnb.entity.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Booking {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomId", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotelId", nullable = false)
    private Hotel hotel;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId",nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer roomsCount;

    @Column(nullable = false)
    private LocalDateTime checkInDate;

    @Column(nullable = false)
    private LocalDateTime checkOutDate;

    @CreationTimestamp
    @Column(updatable=false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="paymentId")
    private Payment payment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="booking_guest",
            joinColumns = @JoinColumn(name="bookingId"),
            inverseJoinColumns = @JoinColumn(name="guestId")
    )
    private Set<Guest> guests;


}
