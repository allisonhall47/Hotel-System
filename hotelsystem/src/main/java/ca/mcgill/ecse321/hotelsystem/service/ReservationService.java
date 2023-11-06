package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.CheckInStatus;
import ca.mcgill.ecse321.hotelsystem.Model.Customer;
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
//        if (reservations == null || reservations.isEmpty()){
//            throw new HRSException(HttpStatus.NOT_FOUND, "There are no reservations in the system.");
//        }
        return reservations;
    }

    /**
     * getReservation: get reservation with id
     * @param id unique id
     * @return reservation
     */
    @Transactional
    public Reservation getReservation(int id) {
        Reservation res = reservationRepository.findReservationByReservationID(id);
        if(res == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, "reservation not in the system.");
        }
        return res;
    }

    /**
     * createReservation: service method to create a reservation and assign it to a customer
     * @param reservation reservation to be created
     * @return a created reservation
     */
    @Transactional
    public Reservation createReservation(Reservation reservation) {
        isValid(reservation);
        //LOOK CONTROLLER FOR RESERVATION CREATERESERVATION
//        if(reservationRepository.findReservationByReservationID(reservation.getReservationID()) != null) {
//            throw new HRSException(HttpStatus.CONFLICT, "reservation with id already exists");
//        }
        //assume customer is valid
        //reservation.setCustomer(customer);
        return reservationRepository.save(reservation);
    }

    private void isValid(Reservation reservation) {
        if(reservation.getCheckIn().isAfter(reservation.getCheckOut())){
            throw new HRSException(HttpStatus.BAD_REQUEST, "invalid checkIn/checkOut dates");
        }
        if(reservation.getNumPeople() <= 0 || reservation.getTotalPrice() <= 0) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "invalid integer");
        }
    }

    /**
     * deleteReservation: deletes a reservation
     * @param reservation reservation to be deleted
     */
    @Transactional
    public void deleteReservation(Reservation reservation) {
        reservation = reservationRepository.findReservationByReservationID(reservation.getReservationID());
//        if(reservation == null) {
//            throw new HRSException(HttpStatus.NOT_FOUND, "reservation does not exist");
//        }
        reservationRepository.delete(reservation);
    }

    /**
     * getReservationsByCustomer: service method to get reservations of a customer
     * @param customer customer to select reservations
     * @return list of reservations
     */
    @Transactional
    public List<Reservation> getReservationsByCustomer(Customer customer) {
        //no need to if check customer is valid
        //already checked when we use customerService.getCustomerByEmail(customerEmail) in the controller
        List<Reservation> list = reservationRepository.getReservationByCustomerEmail(customer.getEmail());
        if(list == null || list.isEmpty()) {
            throw new HRSException(HttpStatus.NOT_FOUND, "no reservation for customer with email: " + customer.getEmail());
        }
        return list;
    }

    /**
     * payReservation: method to pay a reservation and returns change
     * @param reservation reservation to be paid
     * @param money amount of money provided
     * @return change
     */
    @Transactional
    public Reservation payReservation(Reservation reservation, int money) {
        if(reservation == null) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "reservation cannot be null");
        }

        reservation = reservationRepository.findReservationByReservationID(reservation.getReservationID());
        if(reservation == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, "reservation does not exist");
        }
        if(reservation.isPaid()) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "already paid");
        }
        if(money < reservation.getTotalPrice()) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "money not sufficient");
        }

        reservation.setPaid(true);
        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation checkIn(Reservation reservation) {
        reservation = reservationRepository.findReservationByReservationID(reservation.getReservationID());
        if(reservation == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, "reservation does not exist");
        }

        reservation.setCheckedIn(CheckInStatus.CheckedIn);
        return reservationRepository.save(reservation);
    }

    @Transactional
    public List<Reservation> getReservationsNotPaid() {
        List<Reservation> list = reservationRepository.getReservationByPaidIs(false);
        if(list == null || list.isEmpty()) {
            throw new HRSException(HttpStatus.NOT_FOUND, "all reservations are paid");
        }
        return list;
    }

    //TODO if other methods are needed, add
}
