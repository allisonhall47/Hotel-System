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
@CrossOrigin(origins="*")
@RestController
public class RequestController {

    @Autowired
    private RequestService service;

    /**
     * Get all requests currently in the databasr
     * @return List a requests
     */
    @GetMapping ("/request")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestResponseDto> getAllRequests() {
        return service.getAllRequests().stream().map(req -> new RequestResponseDto(req)).toList();
    }

    /**
     * Get the request with the given id
     * @param id Id of the request
     * @throws ca.mcgill.ecse321.hotelsystem.exception.HRSException if no request with the given id exists
     * @return request
     */
    @GetMapping ("/request/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RequestResponseDto getRequestWithId(@PathVariable int id) {
        return new RequestResponseDto(service.getRequest(id));
    }

    /**
     * Get all requests associated with the given reservation
     * @param id Id of the reservation
     * @return
     */
    @GetMapping ("request/reservation/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestResponseDto> getRequestsForReservation(@PathVariable int id) {
        return service.getRequestsForReservationWithId(id).stream().map(req -> new RequestResponseDto(req)).toList();
    }

    /**
     * Create a new request in the database
     * @param req Data used to create the request
     * @return Newly created request
     */
    @PostMapping ("request/new")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestResponseDto createRequest(@RequestBody  RequestRequestDto req) {
        Request request = service.createRequest(req.getDescription(), req.getReservationId());
        return new RequestResponseDto(request);
    }

    /**
     * Change the status of the given request
     * @param id Id of the request whose status should be changed
     * @param status New status
     * @throws ca.mcgill.ecse321.hotelsystem.exception.HRSException if the status is null or no request with the given id exists
     * @return Updated request
     */
    @PostMapping ("request/status/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RequestResponseDto changeRequestStatus(@PathVariable int id, @RequestBody CompletionStatus status) {
        return new RequestResponseDto(service.changeRequestStatus(id, status));
    }

    /**
     * Delete the request with the given id
     * @param id Id of the request
     * @throws ca.mcgill.ecse321.hotelsystem.exception.HRSException if no request with the given id exists
     * @return Redirect message to the list of all requests ("redirect:/request")
     */
    @DeleteMapping("request/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteRequest(@PathVariable int id) {
        service.deleteRequest(id);
        return "redirect:/request"; //TODO: review
    }

    /**
     * Delete all requests assoicated with the given reservation
     * @param id Of the reservation
     * @throws ca.mcgill.ecse321.hotelsystem.exception.HRSException if no reservation with the given id exists
     * @return Redirect message to reservation: "redirect:/reservation/" + id
     */
    @DeleteMapping("request/reservation/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteRequestsForReservation(@PathVariable int id) {
        service.deleteRequestsForReservationWithId(id);
        return "redirect:/reservation/" + id; //TODO: review
    }
}
