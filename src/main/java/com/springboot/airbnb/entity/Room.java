package com.springboot.airbnb.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @Column(nullable = false)
    private String type;

    @Column(nullable=false,precision=10,scale=2)
    private BigDecimal basePrice;

    @OneToMany(mappedBy = "room",fetch = FetchType.EAGER,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Photo> photos;

    @OneToMany(mappedBy = "room",fetch = FetchType.EAGER,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Amnety> amenities;

    @Column(nullable=false)
    private Integer totalCount;

    @Column(nullable=false)
    private Integer capacity;


    @CreationTimestamp
    @Column(updatable=false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
