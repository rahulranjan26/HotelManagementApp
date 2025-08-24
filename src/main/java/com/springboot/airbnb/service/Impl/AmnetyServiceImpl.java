package com.springboot.airbnb.service.Impl;


import com.springboot.airbnb.entity.Amnety;
import com.springboot.airbnb.entity.Hotel;
import com.springboot.airbnb.entity.Room;
import com.springboot.airbnb.repository.AmnetyRepository;
import com.springboot.airbnb.service.AmnetyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AmnetyServiceImpl implements AmnetyService {

    private final AmnetyRepository amnetyRepository;


    @Override
    public Amnety createNewAmnetyForRoom(Room room, Amnety amnety) {
        Amnety newAmnety = new Amnety();
        newAmnety.setRoom(room);
        newAmnety.setAmnetyName(amnety.getAmnetyName());
        return amnetyRepository.save(newAmnety);
    }

    @Override
    public Amnety getAmnetyOfRoomById(Room room, Long amnetyId) {
        Amnety amnety = amnetyRepository.getOne(amnetyId);
        return amnety;
    }

    @Override
    public Boolean deleteAmnetyOfRoomById(Room room, Long amnetyId) {
        amnetyRepository.deleteById(amnetyId);
        return true;
    }

    @Override
    public Amnety createNewAmnetyForHotel(Hotel hotel, Amnety amnety) {
        Amnety newAmnety = new Amnety();
        newAmnety.setHotel(hotel);
        newAmnety.setAmnetyName(amnety.getAmnetyName());
        return amnetyRepository.save(newAmnety);
    }

    @Override
    public Amnety getAmnetyOfHotelById(Hotel hotel, Long amnetyId) {
        Amnety amnety = amnetyRepository.getOne(amnetyId);
        return amnety;
    }

    @Override
    public Boolean deleteAmnetyOfHotelById(Hotel hotel, Long amnetyId) {
        amnetyRepository.deleteById(amnetyId);
        return true;
    }
}
