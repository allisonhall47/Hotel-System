package ca.mcgill.ecse321.hotelsystem.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.mcgill.ecse321.hotelsystem.Model.Reservation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ReservationRepositoryTests {
    @Autowired
    private ReservationRepository reservationRepository;

    @AfterEach
    public void clearDatabase() {
        reservationRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadReservation() {
        // Create reservation.
        String name = "Muffin Man";
        Integer age = 40;
        String address = "123 Drury Lane";
        Reservation reservation = new Reservation();
        reservation.setCheckedIn();
        reservation.setCheckOut();
        reservation.setCustomer();
        reservation.setPaid();
        reservation.setNumPeople();
        rese
        person.setAge(age);
        person.setAddress(address);

        // Save person
        personRepository.save(person);

        // Read person from database.
        person = personRepository.findPersonByName(name);

        // Assert that person is not null and has correct attributes.
        assertNotNull(person);
        assertEquals(name, person.getName());
        assertEquals(age, person.getAge());
        assertEquals(address, person.getAddress());
    }
}