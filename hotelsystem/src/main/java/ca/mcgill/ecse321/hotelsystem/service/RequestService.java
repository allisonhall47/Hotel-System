package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.*;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.repository.RequestRepository;
import ca.mcgill.ecse321.hotelsystem.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    /**
     * Create a new request in the databse
     * @param description Description of the request
     * @param reservationId Id of the reservation associated with this request
     * @throws HRSException if the description is invalid or no reservation with the given id exists
     * @return Newly created request
     */
    @Transactional
    public Request createRequest(String description, int reservationId) {
        if (description == null || description.isBlank() || description.length() < 10) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "Invalid request description");
        }
        Reservation res = checkReservationExistance(reservationId);
        return requestRepository.save(new Request(CompletionStatus.Pending, description, res));
    }

    /**
     * Get the request with the given id
     * @param id Id of the request
     * @throws HRSException if no request with the given id exists
     * @return request
     */
    @Transactional
    public Request getRequest(int id) {
        return checkRequestExistance(id);
    }

    /**
     * Change the status of a request
     * @param id Id of the request
     * @param status New status of the request
     * @return Updated request
     */
    @Transactional
    public Request changeRequestStatus(int id, CompletionStatus status) {
        if (status == null) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "Invalid request status (null)");
        }
        Request req = checkRequestExistance(id);

        req.setStatus(status);
        requestRepository.save(req);

        return req;
    }

    /**
     * Get all requests associated with the given reservation
     * @param id Id of the reservation
     * @throws HRSException if no reservation with the given id exists
     * @return List of requests associated with the reservation
     */
    @Transactional
    public List<Request> getRequestsForReservationWithId(int id) {
        checkReservationExistance(id);
        return requestRepository.findRequestsByReservation_ReservationID(id);
    }

    /**
     * Delete the given request
     * @param id Id of the request
     * @throws HRSException if no request with the given id exists
     */
    @Transactional
    public void deleteRequest(int id) {
        checkRequestExistance(id);
        requestRepository.deleteRequestByRequestId(id);
    }

    /**
     * Delete all requests for the given reservation
     * @param id Id of the reservation
     * @throws HRSException if no reservation with the given id exists
     */
    @Transactional
    public void deleteRequestsForReservationWithId(int id) {
        checkReservationExistance(id);
        requestRepository.deleteRequestsByReservation_ReservationID(id);
    }

    /**
     * Get all requests from the database
     * @return List of requests
     */
    @Transactional
    public List<Request> getAllRequests(){
        List<Request> requests = requestRepository.findAll();
        return requests;
    }

    private Request checkRequestExistance(int id) {
        Request req = requestRepository.findRequestByRequestId(id);
        if (req == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, String.format("No request with id %d", id));
        }
        return req;
    }

    private Reservation checkReservationExistance(int id) {
        Reservation res = reservationRepository.findReservationByReservationID(id);
        if (res == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, String.format("No reservation with id %d", id));
        }
        return res;
    }
}
