package ca.mcgill.ecse321.hotelsystem.controller;

import ca.mcgill.ecse321.hotelsystem.dto.RepairRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.RepairResponseDto;
import ca.mcgill.ecse321.hotelsystem.service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RepairController {

    @Autowired
    private RepairService repairService;

    @GetMapping (value = { "/repairs", "/repairs /" })
    public List<RepairResponseDto> getAllRepairs() {
        return service.getAllPersons().stream().map(p -> convertToDto(p)).collect(Collectors.toList());
    }

    @PostMapping("/repair/new")
    public RepairResponseDto createRepair(@RequestBody RepairRequestDto reqRepair) {
        repairService.createRepair(reqRepair.e)
        Reservation res = resRoom.toModel(reservationService.getReservation(resRoom.getLinkedReservationId()), specificRoomService.findSpecificRoomByNumber(resRoom.getRoomNumber()));
        ReservedRoom newRoom = reservedRoomService.createReservedRoom(room);
        return new ReservedRoomResponseDto(newRoom);
    }

}
