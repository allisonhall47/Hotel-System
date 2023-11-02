package ca.mcgill.ecse321.hotelsystem.controller;

import ca.mcgill.ecse321.hotelsystem.dto.RequestRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.RequestResponseDto;
import ca.mcgill.ecse321.hotelsystem.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RequestController {

    @Autowired
    private RequestService service;

    @GetMapping (value={"/requests", "/requests/"})
    public List<RequestResponseDto> getAllRequests() {
        return service.getAllRequests().stream().map(req -> new RequestResponseDto(req)).toList();
    }

    @GetMapping (value={"/requests/{id}"})
    public RequestResponseDto getRequestWithId(@PathVariable int id) {
        return new RequestResponseDto(service.getRequest(id));
    }

    @GetMapping (value={"requests/reservation/{id}"})
    public List<RequestResponseDto>mgetRequestsForReservation(@PathVariable int id) {
        return service.getRequestsForReservationWithId(id).stream().map(req -> new RequestResponseDto(req)).toList();
    }

    @PostMapping (value={"requests/new"})
    public RequestResponseDto createRequest(RequestRequestDto ) {
        service.createRequest()
    }




}
