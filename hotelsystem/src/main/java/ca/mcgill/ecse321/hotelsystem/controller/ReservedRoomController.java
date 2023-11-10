package ca.mcgill.ecse321.hotelsystem.controller;

import ca.mcgill.ecse321.hotelsystem.Model.Reservation;
import ca.mcgill.ecse321.hotelsystem.Model.ReservedRoom;
import ca.mcgill.ecse321.hotelsystem.Model.SpecificRoom;
import ca.mcgill.ecse321.hotelsystem.dto.ReservationRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.ReservedRoomRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.ReservedRoomResponseDto;
import ca.mcgill.ecse321.hotelsystem.dto.SpecificRoomRequestDto;
import ca.mcgill.ecse321.hotelsystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ReservedRoomController {

    @Autowired
    ReservedRoomService reservedRoomService;

    @Autowired
    SpecificRoomService specificRoomService;

    @Autowired
    ReservationService reservationService;

    @Autowired
    RoomService roomService;
    @Autowired
    CustomerService customerService;

    /**
     * get all reserved rooms
     * @return list of dto objects
     */
    @GetMapping("/reservedRoom")
    public List<ReservedRoomResponseDto> getAllReservedRooms() {
        List<ReservedRoom> list = reservedRoomService.getAllReservedRooms();
        List<ReservedRoomResponseDto> dtos = new ArrayList<>();
        for(ReservedRoom room : list) {
            dtos.add(new ReservedRoomResponseDto(room));
        }
        return dtos;
    }

    /**
     * create new reserved room
     * @param resRoom info of new reserved room
     * @return new dto object
     */
    @PostMapping("/reservedRoom/new")
    public ReservedRoomResponseDto createReservedRoom(@RequestBody ReservedRoomRequestDto resRoom) {
        ReservedRoom room = resRoom.toModel(reservationService.getReservation(resRoom.getLinkedReservationId()), specificRoomService.findSpecificRoomByNumber(resRoom.getRoomNumber()));
        ReservedRoom newRoom = reservedRoomService.createReservedRoom(room);
        return new ReservedRoomResponseDto(newRoom);
    }

    /**
     * get reserved rooom by id
     * @param id id
     * @return dto object
     */
    @GetMapping("/reservedRoom/{id}")
    public ReservedRoomResponseDto getReservedRoomById(@PathVariable(value = "id") int id){
        return new ReservedRoomResponseDto(reservedRoomService.getReservedRoomById(id));
    }

    /**
     * get reserved rooms for a reservation
     * @param id reservation id
     * @return list of dto object
     */
    @GetMapping("/reservedRoom/reservation/{reservationId}")
    public List<ReservedRoomResponseDto> getReservedRoomsByReservation(@PathVariable("reservationId") int id) {
        List<ReservedRoom> list = reservedRoomService.getReservedRoomsByReservation(id);
        List<ReservedRoomResponseDto> dtos = new ArrayList<>();
        for(ReservedRoom room : list) {
            dtos.add(new ReservedRoomResponseDto(room));
        }
        return dtos;
    }

    /**
     * get reserved rooms by specific room
     * @param number room number
     * @return list of dto objects
     */
    @GetMapping("/reservedRoom/specificRoom/{specRoomNumber}")
    public List<ReservedRoomResponseDto> getReservedRoomBySpecRoom(@PathVariable("specRoomNumber") int number) {
        SpecificRoom specRoom = specificRoomService.findSpecificRoomByNumber(number);
        List<ReservedRoom> list = reservedRoomService.getReservedRoomsBySpecRoom(specRoom);
        List<ReservedRoomResponseDto> dtos = new ArrayList<>();
        for(ReservedRoom room : list) {
            dtos.add(new ReservedRoomResponseDto(room));
        }
        return dtos;
    }

    /**
     * delete reserved room
     * @param id id
     */
    @DeleteMapping("/reservedRoom/{id}")
    public void deleteReservedRoom(@PathVariable("id") int id) {
        ReservedRoom room = reservedRoomService.getReservedRoomById(id);
        reservedRoomService.deleteReservedRoom(room);
    }
}


