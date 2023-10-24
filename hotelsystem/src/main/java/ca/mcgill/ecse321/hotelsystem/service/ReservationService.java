package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.Reservation;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    @Autowired
    ReservationRepository reservationRepository;

    /**
     * GetAllReservations: service method to fetch all existing reservations in the database
     * @return List of reservations
     * @throws HRSException if no reservations exist in the system
     */
    @Transactional
    public List<Reservation> getAllReservations(){
        List<Reservation> reservations = reservationRepository.findAll();
        if (reservations.size() == 0){
            throw new HRSException(HttpStatus.NOT_FOUND, "There are no reservations in the system.");
        }
        return reservations;
    }
}
