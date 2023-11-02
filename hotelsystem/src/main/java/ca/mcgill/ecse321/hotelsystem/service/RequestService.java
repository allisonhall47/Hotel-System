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
    RequestRepository requestRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Transactional
    public Request createRequest(String description, int reservationId) {
        if (description == null || description.length() == 0) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "Invalid request description (empty)");
        }
        Reservation res = reservationRepository.findReservationByReservationID(reservationId);
        if (res == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, String.format("No reservation with id %d", reservationId));
        }
        return requestRepository.save(new Request(CompletionStatus.Pending, description, res));
    }

    @Transactional
    public Request getRequest(int id) {
        Request req = requestRepository.findRequestByRequestId(id);
        if (req == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, String.format("No request with id %d", id));
        }
        return req;
    }

    @Transactional
    public Request changeRequestStatus(int id, CompletionStatus status) {
        if (status == null) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "Invalid request status (null)");
        }
        Request req = requestRepository.findRequestByRequestId(id);
        if (req == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, String.format("No request with id %d", id));
        }

        req.setStatus(status);
        requestRepository.save(req);

        return req;
    }

    @Transactional
    public List<Request> getRequestsForReservationWithId(int id) {
        return requestRepository.findRequestsByReservation_ReservationID(id);
    }

    @Transactional
    public void deleteRequest(int id) {
        Request req = requestRepository.findRequestByRequestId(id);
        if (req == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, String.format("No request with id %d", id));
        }
        requestRepository.deleteRequestByRequestId(id);
    }

    @Transactional
    public void deleteRequestsForReservationWithId(int id) {
        requestRepository.deleteRequestsByReservation_ReservationID(id);
    }

    /**
     * GetAllRequests: service method to fetch all existing request in the database
     * @return List of requests
     * @throws HRSException if no requests exist in the system
     */
    @Transactional
    public List<Request> getAllRequests(){
        List<Request> requests = requestRepository.findAll();
        if (requests.size() == 0){
            throw new HRSException(HttpStatus.NOT_FOUND, "There are no owners in the system.");
        }
        return requests;
    }
}
