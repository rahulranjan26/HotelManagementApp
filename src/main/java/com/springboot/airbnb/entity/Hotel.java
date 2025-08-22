package com.springboot.airbnb.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Data
@Table(name="hotel")
public class Hotel {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long hotelId;

    @Column(nullable = false)
    private String name;

    private String city;

    @OneToMany(mappedBy = "hotel",fetch = FetchType.EAGER,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Photo> photos;

    @OneToMany(mappedBy = "hotel",fetch = FetchType.EAGER,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Amnety> amenities;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @Embedded
    private HotelContactInfo hotelContactInfo;
    

    @OneToMany(mappedBy = "hotel", orphanRemoval=true)
    private List<Room> rooms;

    @Column(nullable=false)
    private Boolean isActive;

    @ManyToOne
    private User owner;


}
