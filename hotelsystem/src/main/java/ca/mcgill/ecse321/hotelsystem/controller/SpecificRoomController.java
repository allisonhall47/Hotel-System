package ca.mcgill.ecse321.hotelsystem.controller;

import ca.mcgill.ecse321.hotelsystem.Model.Room;
import ca.mcgill.ecse321.hotelsystem.Model.SpecificRoom;
import ca.mcgill.ecse321.hotelsystem.Model.ViewType;
import ca.mcgill.ecse321.hotelsystem.dto.ReservationRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.SpecificRoomAvailableDto;
import ca.mcgill.ecse321.hotelsystem.dto.SpecificRoomRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.SpecificRoomResponseDto;
import ca.mcgill.ecse321.hotelsystem.service.RoomService;
import ca.mcgill.ecse321.hotelsystem.service.SpecificRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins="*")
@RestController
public class SpecificRoomController {
    @Autowired
    private SpecificRoomService specificRoomService;

    @Autowired
    private RoomService roomService;

    /**
     * getAllRooms: get a list of all specific rooms in the system
     * @return a list of SpecificRoomResponseDto
     */
    @GetMapping(value = "/specificRooms")
    public List<SpecificRoomResponseDto> getAllRooms(){
        return specificRoomService.getAllSpecificRooms().stream().map(SpecificRoomResponseDto::new).collect(Collectors.toList());
    }

    /**
     * getAllRooms: get a list of all specific rooms in the system by type
     * @param type: type of room
     * @return a list of SpecificRoomResponseDto
     */
    @GetMapping(value = "/specificRoom/type/{type}")
    public List<SpecificRoomResponseDto> getSpecificRoomsByRoomType(@PathVariable String type){
        List<SpecificRoom> specificRooms = specificRoomService.findSpecificRoomsByRoomType(type);
        return specificRooms.stream().map(SpecificRoomResponseDto::new).collect(Collectors.toList());
    }

    @GetMapping(value = "/specificRoom/available/type/{type}/{checkIn}/{checkOut}")
    public List<SpecificRoomResponseDto> getAvailableSpecificRoomsByRoomType(@PathVariable String type,@PathVariable LocalDate checkIn,  @PathVariable LocalDate checkOut){
        List<SpecificRoom> specificRooms = specificRoomService.getAvailableSpecificRoomByType(checkIn, checkOut, type);
        return specificRooms.stream().map(SpecificRoomResponseDto::new).collect(Collectors.toList());
    }

    /**
     * getAllRooms: get a list of all specific rooms in the system by view
     * @param view: type of room
     * @return a list of SpecificRoomResponseDto
     */
    @GetMapping(value = "/specificRoom/view/{view}")
    public List<SpecificRoomResponseDto> getSpecificRoomsByRoomType(@PathVariable ViewType view){
        List<SpecificRoom> specificRooms = specificRoomService.findSpecificRoomsByView(view);
        return specificRooms.stream().map(SpecificRoomResponseDto::new).collect(Collectors.toList());
    }

    /**
     * getAllRooms: get a list of all specific rooms in the system open for use
     * @return a list of SpecificRoomResponseDto
     */
    @GetMapping(value = "/specificRoom/openForUse")
    public List<SpecificRoomResponseDto> getSpecificRoomsByRoomType(){
        List<SpecificRoom> specificRooms = specificRoomService.findSpecificRoomsOpenForUse();
        return specificRooms.stream().map(SpecificRoomResponseDto::new).collect(Collectors.toList());
    }

    /**
     * getAllRooms: get a specific room in the system by number
     * @param number: number of room
     * @return a SpecificRoomResponseDto
     */
    @GetMapping(value = "/specificRoom/number/{number}")
    public ResponseEntity<SpecificRoomResponseDto> getSpecificRoomsByRoomType(@PathVariable int number){
        SpecificRoom specificRoom = specificRoomService.findSpecificRoomByNumber(number);
        return new ResponseEntity<>(new SpecificRoomResponseDto(specificRoom), HttpStatus.OK);
    }

    /**
     * createSpecificRoom: create new specificRoom
     * @param specificRoomRequestDto: new room details
     * @return a SpecificRoomResponseDto
     */
    @PostMapping(value = "/specificRoom/create")
    public ResponseEntity<SpecificRoomResponseDto> createSpecificRoom(@RequestBody SpecificRoomRequestDto specificRoomRequestDto){
        String type = specificRoomRequestDto.getRoomType();
        Room room = roomService.getRoomByType(type);
        SpecificRoom specificRoom = specificRoomRequestDto.toModel(room);
        specificRoom = specificRoomService.createSpecificRoom(specificRoom);
        return new ResponseEntity<>(new SpecificRoomResponseDto(specificRoom), HttpStatus.CREATED);
    }

    /**
     * createSpecificRoom: update a specificRoom
     * @param specificRoomRequestDto: new details
     * @return a SpecificRoomResponseDto
     */
    @PutMapping(value = "/specificRoom/update")
    public ResponseEntity<SpecificRoomResponseDto> updateSpecificRoom(@RequestBody SpecificRoomRequestDto specificRoomRequestDto){
        String type = specificRoomRequestDto.getRoomType();
        Room room = roomService.getRoomByType(type);
        SpecificRoom specificRoom = specificRoomRequestDto.toModel(room);
        specificRoom = specificRoomService.updateSpecificRoom(specificRoom);
        return new ResponseEntity<>(new SpecificRoomResponseDto(specificRoom), HttpStatus.OK);
    }

    /**
     * createSpecificRoom: delete a specificRoom by number
     * @param number: number of room
     */
    @DeleteMapping(value = "/specificRoom/delete/{number}")
    public void deleteSpecificRoom(@PathVariable int number){
        specificRoomService.deleteSpecificRoomByNumber(number);
    }
}
