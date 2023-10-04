package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.CheckInStatus;
import ca.mcgill.ecse321.hotelsystem.Model.Customer;
import ca.mcgill.ecse321.hotelsystem.Model.Reservation;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReservationRepositoryTests {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
    public void clearDatabase() {
        reservationRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadReservation() {
        Customer customer = new Customer("bill@gmail.com", "Bill", null, 0);
        customer = customerRepository.save(customer);

        Reservation reservation = new Reservation(2, Date.valueOf("2023-10-23"), Date.valueOf("2023-11-01"), 1400, false, CheckInStatus.BeforeCheckIn, customer);
        reservation = reservationRepository.save(reservation);

        Reservation reservationRep = reservationRepository.findReservationByReservationID(reservation.getReservationID());

        assertNotNull(reservationRep);
        assertEquals(reservation.getCheckIn(), reservationRep.getCheckIn());
        assertEquals(reservation.getCheckOut(), reservationRep.getCheckOut());
        assertEquals(2, reservationRep.getNumPeople());
        assertFalse(reservationRep.isPaid());
    }

    @Test
    public void testFindByCheckInDate(){
        Customer customer = new Customer("bill@gmail.com", "Bill", null, 0);
        customer = customerRepository.save(customer);


        Date checkInDate = Date.valueOf("2023-10-23");
        Date checkOutDate = Date.valueOf("2023-11-01");
        Reservation reservation = new Reservation(2, checkInDate, checkOutDate, 1400, false, CheckInStatus.BeforeCheckIn, customer);
        reservation = reservationRepository.save(reservation);


        checkOutDate = Date.valueOf("2023-11-05");
        Reservation reservation2 = new Reservation(2, checkInDate, checkOutDate, 1700, false, CheckInStatus.BeforeCheckIn, customer);

        reservation2 = reservationRepository.save(reservation2);

        List<Reservation> reservations = reservationRepository.findReservationsByCheckin(checkInDate);
        assertEquals(2, reservations.size());
        assertEquals(Date.valueOf("2023-11-01"), reservations.get(0).getCheckOut());
        assertEquals(Date.valueOf("2023-11-05"), reservations.get(1).getCheckOut());
    }


    @Test
    public void testFindReservationsByCustomerEmail(){
        Customer customer = new Customer("bill@gmail.com", "Bill", null, 0);
        customer = customerRepository.save(customer);

        Date checkInDate = Date.valueOf("2023-10-23");
        Date checkOutDate = Date.valueOf("2023-11-01");
        Reservation reservation = new Reservation(2, checkInDate, checkOutDate, 1400, false, CheckInStatus.BeforeCheckIn, customer);
        reservation = reservationRepository.save(reservation);

        checkOutDate = Date.valueOf("2023-11-05");
        Reservation reservation2 = new Reservation(2, checkInDate, checkOutDate, 1700, false, CheckInStatus.BeforeCheckIn, customer);
        reservation2 = reservationRepository.save(reservation2);

        List<Reservation> reservations = reservationRepository.findReservationsByCustomerEmail("bill@gmail.com");
        assertEquals(2, reservations.size());
        assertEquals(Date.valueOf("2023-11-01"), reservations.get(0).getCheckOut());
        assertEquals(Date.valueOf("2023-11-05"), reservations.get(1).getCheckOut());
    }

    @Test
    @Transactional
    public void testDeleteById(){
        Customer customer = new Customer("bill@gmail.com", "Bill", null, 0);
        customer = customerRepository.save(customer);

        Date checkInDate = Date.valueOf("2023-10-23");
        Date checkOutDate = Date.valueOf("2023-11-01");
        Reservation reservation = new Reservation(2, checkInDate, checkOutDate, 1400, false, CheckInStatus.BeforeCheckIn, customer);
        reservation = reservationRepository.save(reservation);
        int reservationID = reservation.getReservationID();

        Reservation reservationRep = reservationRepository.findReservationByReservationID(reservationID);

        assertNotNull(reservationRep);
        reservationRepository.deleteReservationByReservationID(reservationID);

        reservationRep = reservationRepository.findReservationByReservationID(reservation.getReservationID());
        assertNull(reservationRep);
    }
}