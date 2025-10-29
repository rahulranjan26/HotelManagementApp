package com.springboot.airbnb.controller;

import com.springboot.airbnb.dto.RoomDto;
import com.springboot.airbnb.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/hotels/{hotelId}/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomDto> createRoom(@PathVariable Long hotelId, @RequestBody RoomDto roomDto) {
        return new ResponseEntity<>(roomService.createNewRoom(hotelId, roomDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<RoomDto>> getAllRoomInHotel(@PathVariable Long hotelId) {
        return new ResponseEntity<>(roomService.getAllRoomInHotel(hotelId), HttpStatus.OK);
    }

    @GetMapping(path = "/{roomId}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long hotelId, @PathVariable Long roomId) {
        return new ResponseEntity<>(roomService.getRoomById(roomId), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{roomId}")
    public ResponseEntity<Boolean> deleteRoomById(@PathVariable Long hotelId, @PathVariable Long roomId) {
        return new ResponseEntity<>(roomService.deleteRoomById(roomId), HttpStatus.OK);
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<RoomDto> updateRoomById(@PathVariable Long hotelId,
                                                  @PathVariable Long roomId,
                                                  @RequestBody RoomDto roomDto) {
        return ResponseEntity.ok(roomService.updateRoomById(hotelId,roomId,roomDto));
    }

}
