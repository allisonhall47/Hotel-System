package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.Reservation;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.List;

public interface ReservationRepository extends CrudRepository<Reservation, Integer> {
    Reservation findReservationByReservationID(int id);
    List<Reservation> findReservationsByCheckin(Date date);
    List<Reservation> findReservationsByCustomerEmail(String email); // potentially customer_email
    void deleteByReservationID(int id);

}
