package ca.mcgill.ecse321.hotelsystem.controller;

import ca.mcgill.ecse321.hotelsystem.Model.Room;
import ca.mcgill.ecse321.hotelsystem.Model.SpecificRoom;
import ca.mcgill.ecse321.hotelsystem.Model.ViewType;
import ca.mcgill.ecse321.hotelsystem.dto.SpecificRoomRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.SpecificRoomResponseDto;
import ca.mcgill.ecse321.hotelsystem.service.RoomService;
import ca.mcgill.ecse321.hotelsystem.service.SpecificRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins="*")
@RestController
public class SpecificRoomController {
    @Autowired
    private SpecificRoomService specificRoomService;

    @Autowired
    private RoomService roomService;

    @GetMapping(value = "/specificRooms")
    public List<SpecificRoomResponseDto> getAllRooms(){
        return specificRoomService.getAllSpecificRooms().stream().map(SpecificRoomResponseDto::new).collect(Collectors.toList());
    }

    @GetMapping(value = "/specificRoom/{type}")
    public List<SpecificRoomResponseDto> getSpecificRoomsByRoomType(@PathVariable String type){
        List<SpecificRoom> specificRooms = specificRoomService.findSpecificRoomsByRoomType(type);
        return specificRooms.stream().map(SpecificRoomResponseDto::new).collect(Collectors.toList());
    }

    @GetMapping(value = "/specificRoom/{view}")
    public List<SpecificRoomResponseDto> getSpecificRoomsByRoomType(@PathVariable ViewType view){
        List<SpecificRoom> specificRooms = specificRoomService.findSpecificRoomsByView(view);
        return specificRooms.stream().map(SpecificRoomResponseDto::new).collect(Collectors.toList());
    }

    @GetMapping(value = "/specificRoom/openForUse")
    public List<SpecificRoomResponseDto> getSpecificRoomsByRoomType(){
        List<SpecificRoom> specificRooms = specificRoomService.findSpecificRoomsOpenForUse();
        return specificRooms.stream().map(SpecificRoomResponseDto::new).collect(Collectors.toList());
    }

    @GetMapping(value = "/specificRoom/{number}")
    public ResponseEntity<SpecificRoomResponseDto> getSpecificRoomsByRoomType(@PathVariable int number){
        SpecificRoom specificRoom = specificRoomService.findSpecificRoomByNumber(number);
        return new ResponseEntity<>(new SpecificRoomResponseDto(specificRoom), HttpStatus.OK);
    }

    @PostMapping(value = "/specificRoom/create")
    public ResponseEntity<SpecificRoomResponseDto> createSpecificRoom(@RequestBody SpecificRoomRequestDto specificRoomRequestDto){
        String type = specificRoomRequestDto.getRoomType();
        Room room = roomService.getRoomByType(type);
        SpecificRoom specificRoom = specificRoomRequestDto.toModel(room);
        specificRoom = specificRoomService.createSpecificRoom(specificRoom);
        return new ResponseEntity<>(new SpecificRoomResponseDto(specificRoom), HttpStatus.CREATED);
    }

    @PutMapping(value = "/specificRoom/update")
    public ResponseEntity<SpecificRoomResponseDto> updateSpecificRoom(@RequestBody SpecificRoomRequestDto specificRoomRequestDto){
        String type = specificRoomRequestDto.getRoomType();
        Room room = roomService.getRoomByType(type);
        SpecificRoom specificRoom = specificRoomRequestDto.toModel(room);
        specificRoom = specificRoomService.updateSpecificRoom(specificRoom);
        return new ResponseEntity<>(new SpecificRoomResponseDto(specificRoom), HttpStatus.OK);
    }

    @DeleteMapping(value = "/specificRoom/delete/{number}")
    public void deleteSpecificRoom(@PathVariable int number){
        specificRoomService.deleteSpecificRoomByNumber(number);
    }
}
