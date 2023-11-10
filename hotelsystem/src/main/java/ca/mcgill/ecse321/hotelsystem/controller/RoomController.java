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

    /**
     * getAllRooms: get a list of all rooms in the system
     * @return a list of RoomResponseDto
     */
    @GetMapping(value = "/rooms")
    public List<RoomResponseDto> getAllRooms(){
        return roomService.getAllRooms().stream().map(RoomResponseDto::new).collect(Collectors.toList());
    }

    /**
     * getRoomByType: get a list of all rooms in the system by type
     * @return a list of RoomResponseDto
     */
    @GetMapping(value = "/room/{type}")
    public ResponseEntity<RoomResponseDto> getRoomByType(@PathVariable String type){
        Room room = roomService.getRoomByType(type);
        return new ResponseEntity<>(new RoomResponseDto(room), HttpStatus.OK);
    }

    /**
     * createRoom: create a room
     * @param roomRequestDto: information for new room
     * @return a RoomResponseDto
     */
    @PostMapping(value = "/room/create")
    public ResponseEntity<RoomResponseDto> createRoom(@RequestBody RoomRequestDto roomRequestDto){
        Room room = roomRequestDto.toModel();
        room = roomService.createRoom(room);
        return new ResponseEntity<>(new RoomResponseDto(room), HttpStatus.CREATED);
    }

    /**
     * updateRoom: update a room
     * @param roomRequestDto: information for updated room
     * @return a RoomResponseDto
     */
    @PutMapping(value = "/room/update")
    public ResponseEntity<RoomResponseDto> updateRoom(@RequestBody RoomRequestDto roomRequestDto){
        Room room = roomRequestDto.toModel();
        room = roomService.updateRoom(room);
        return new ResponseEntity<>(new RoomResponseDto(room), HttpStatus.OK);
    }
}
