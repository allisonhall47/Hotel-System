package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.Request;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.repository.RequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {

    @Autowired
    RequestRepository requestRepository;

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
