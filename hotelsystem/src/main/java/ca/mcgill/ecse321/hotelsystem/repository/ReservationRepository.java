package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.Reservation;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends CrudRepository<Reservation, Integer> {
    Reservation findReservationByReservationID(int id);
    List<Reservation> findReservationsByCheckin(LocalDate date);
    List<Reservation> findReservationsByCustomerEmail(String email); // potentially customer_email

    void deleteReservationByReservationID(int id);

}
