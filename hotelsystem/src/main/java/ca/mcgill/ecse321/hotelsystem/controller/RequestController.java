package ca.mcgill.ecse321.hotelsystem.controller;

import ca.mcgill.ecse321.hotelsystem.Model.CompletionStatus;
import ca.mcgill.ecse321.hotelsystem.Model.Request;
import ca.mcgill.ecse321.hotelsystem.dto.RequestRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.RequestResponseDto;
import ca.mcgill.ecse321.hotelsystem.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RequestController {

    @Autowired
    private RequestService service;

    @GetMapping ("/request")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestResponseDto> getAllRequests() {
        return service.getAllRequests().stream().map(req -> new RequestResponseDto(req)).toList();
    }

    @GetMapping ("/request/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RequestResponseDto getRequestWithId(@PathVariable int id) {
        return new RequestResponseDto(service.getRequest(id));
    }

    @GetMapping ("request/reservation/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestResponseDto> getRequestsForReservation(@PathVariable int id) {
        return service.getRequestsForReservationWithId(id).stream().map(req -> new RequestResponseDto(req)).toList();
    }

    @PostMapping ("request/new")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestResponseDto createRequest(@RequestBody  RequestRequestDto req) {
        Request request = service.createRequest(req.getDescription(), req.getReservationId());
        return new RequestResponseDto(request);
    }

    @PostMapping ("request/status/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RequestResponseDto changeRequestStatus(@PathVariable int id, @RequestBody CompletionStatus status) {
        return new RequestResponseDto(service.changeRequestStatus(id, status));
    }

    @DeleteMapping("request/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteRequest(@PathVariable int id) {
        service.deleteRequest(id);
        return "redirect:/request"; //TODO: review
    }

    @DeleteMapping("request/reservation/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteRequestsForReservation(@PathVariable int id) {
        service.deleteRequestsForReservationWithId(id);
        return "redirect:/reservation/" + id; //TODO: review
    }
}
