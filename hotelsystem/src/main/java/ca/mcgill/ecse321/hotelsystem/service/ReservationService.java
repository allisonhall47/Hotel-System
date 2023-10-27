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
        if (reservations == null || reservations.isEmpty()){
            throw new HRSException(HttpStatus.NOT_FOUND, "There are no reservations in the system.");
        }
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
     * @param customer customer to assign to reservation
     * @return a created reservation
     */
    @Transactional
    public Reservation createReservation(Reservation reservation, Customer customer) {
        isValid(reservation);
        if(reservationRepository.findReservationByReservationID(reservation.getReservationID()) != null) {
            throw new HRSException(HttpStatus.CONFLICT, "reservation with id already exists");
        }
        //TODO assume customer is valid
        reservation.setCustomer(customer);
        return reservationRepository.save(reservation);
    }

    private void isValid(Reservation reservation) {
        if(reservation.getCheckIn().after(reservation.getCheckOut())){
            throw new HRSException(HttpStatus.BAD_REQUEST, "invalid checkIn/checkOut dates");
        }
        if(reservation.getNumPeople() <= 0 || reservation.getTotalPrice() <= 0) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "invalid integer");
        }
        //TODO check customer ???
    }

    /**
     * deleteReservation: deletes a reservation
     * @param reservation reservation to be deleted
     */
    @Transactional
    public void deleteReservation(Reservation reservation) {
        reservation = reservationRepository.findReservationByReservationID(reservation.getReservationID());
        if(reservation == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, "reservation does not exist");
        }
        reservationRepository.delete(reservation);
    }

    /**
     * getReservationsByCustomer: service method to get reservations of a customer
     * @param customer customer to select reservations
     * @return list of reservations
     */
    @Transactional
    public List<Reservation> getReservationsByCustomer(Customer customer) {
        //TODO check customer is valid ??
        return reservationRepository.getReservationByCustomerEmail(customer.getEmail());
    }

    /**
     * payReservation: method to pay a reservation and returns change
     * @param reservation reservation to be paid
     * @param money amount of money provided
     * @return change
     */
    @Transactional
    public int payReservation(Reservation reservation, int money) {
        reservation = reservationRepository.findReservationByReservationID(reservation.getReservationID());
        if(reservation == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, "reservation does not exist");
        }
        if(reservation.isPaid()) {
            throw new IllegalArgumentException("reservation is already paid");
        }
        if(money < reservation.getTotalPrice()) {
            throw new IllegalArgumentException("not sufficient money");
        }

        reservation.setPaid(true);
        reservationRepository.save(reservation);
        return money-reservation.getTotalPrice();
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
