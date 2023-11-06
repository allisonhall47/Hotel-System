package ca.mcgill.ecse321.hotelsystem.controller;

import ca.mcgill.ecse321.hotelsystem.Model.Room;
import ca.mcgill.ecse321.hotelsystem.dto.RoomRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.RoomResponseDto;
import ca.mcgill.ecse321.hotelsystem.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins="*")
@RestController
public class RoomController {
    @Autowired
    private RoomService roomService;

    @GetMapping(value = "/rooms")
    public List<RoomResponseDto> getAllRooms(){
        return roomService.getAllRooms().stream().map(RoomResponseDto::new).collect(Collectors.toList());
    }

    @GetMapping(value = "/room/{type}")
    public ResponseEntity<RoomResponseDto> getRoomByType(@PathVariable String type){
        Room room = roomService.getRoomByType(type);
        return new ResponseEntity<>(new RoomResponseDto(room), HttpStatus.OK);
    }

    @PostMapping(value = "/room/create")
    public ResponseEntity<RoomResponseDto> createRoom(@RequestBody RoomRequestDto roomRequestDto){
        Room room = roomRequestDto.toModel();
        room = roomService.createRoom(room);
        return new ResponseEntity<>(new RoomResponseDto(room), HttpStatus.CREATED);
    }

    @PutMapping(value = "/room/update")
    public ResponseEntity<RoomResponseDto> updateRoom(@RequestBody RoomRequestDto roomRequestDto){
        Room room = roomRequestDto.toModel();
        room = roomService.updateRoom(room);
        return new ResponseEntity<>(new RoomResponseDto(room), HttpStatus.OK);
    }
}
