package com.springboot.airbnb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoId;

    private String photoName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="hotelId")
    @JsonIgnore
    private Hotel hotel;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="roomId")
    @JsonIgnore
    private Room room;

}
