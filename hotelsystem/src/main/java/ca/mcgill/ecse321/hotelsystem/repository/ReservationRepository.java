package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.Reservation;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.List;

public interface ReservationRepository extends CrudRepository<Reservation, Integer> {
    Reservation findReservationByReservationID(int id);
    Reservation findReservationByCheckIn(Date date);
    List<Reservation> findReservationsByCustomer_Email(String email);
    List<Reservation> findAll();
    void deleteByReservationID(int id);

}
