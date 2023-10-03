package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.Customer;
import ca.mcgill.ecse321.hotelsystem.Model.Reservation;
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
        Customer customer = new Customer();
        customer.setEmail("bill@gmail.com");
        customer.setName("Bill");
        customer.setAccount(null);
        customerRepository.save(customer);

        Reservation reservation = new Reservation();
        Date checkInDate = Date.valueOf("2023-10-23");
        Date checkOutDate = Date.valueOf("2023-11-01");
        reservation.setCheckIn(checkInDate);
        reservation.setCheckOut(checkOutDate);
        reservation.setPaid(false);
        reservation.setNumPeople(2);
        reservation.setCustomer(customer);
        reservationRepository.save(reservation);
        Reservation reservationRep = reservationRepository.findReservationByReservationID(reservation.getReservationID());

        assertNotNull(reservationRep);
        assertEquals(checkInDate, reservationRep.getCheckIn());
        assertEquals(checkOutDate, reservationRep.getCheckOut());
        assertEquals(2, reservationRep.getNumPeople());
        assertFalse(reservationRep.isPaid());
    }

    @Test
    public void testFindByCheckInDate(){
        Customer customer = new Customer();
        customer.setEmail("bill@gmail.com");
        customer.setName("Bill");
        customer.setAccount(null);
        customerRepository.save(customer);

        Reservation reservation = new Reservation();
        Date checkInDate = Date.valueOf("2023-10-23");
        Date checkOutDate = Date.valueOf("2023-11-01");
        reservation.setCheckIn(checkInDate);
        reservation.setCheckOut(checkOutDate);
        reservation.setPaid(false);
        reservation.setNumPeople(2);
        reservation.setCustomer(customer);
        reservationRepository.save(reservation);

        Reservation reservation2 = new Reservation();
        checkOutDate = Date.valueOf("2023-11-05");
        reservation2.setCheckIn(checkInDate);
        reservation2.setCheckOut(checkOutDate);
        reservation2.setPaid(true);
        reservation2.setNumPeople(2);
        reservation2.setCustomer(customer);
        reservationRepository.save(reservation2);

        List<Reservation> reservations = reservationRepository.findReservationsByCheckin(checkInDate);

    }
}