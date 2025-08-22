package com.springboot.airbnb.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Data
public class Amnety {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long amnetyId;

    private String amnetyName;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="hotelId")
    @JsonIgnore
    private Hotel hotel;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="roomId")
    @JsonIgnore
    private Room room;

}
