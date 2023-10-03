package ca.mcgill.ecse321.hotelsystem.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.mcgill.ecse321.hotelsystem.Model.CompletionStatus;
import ca.mcgill.ecse321.hotelsystem.Model.Request;
import ca.mcgill.ecse321.hotelsystem.Model.Reservation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RequestRepositoryTests {
    @Autowired
    private RequestRepository requestRepository;

    @AfterEach
    public void clearDatabase() {
        requestRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadRequest() {
        // Create request.
        String description = "Need more towels";
        Reservation reservation10 = new Reservation();
        Request request = new Request();

        request.setReservation(reservation10);
        request.setDescription(description);

        int requestId = request.getRequestId();
        int reservationId = reservation10.getReservationID();
        CompletionStatus status = request.getStatus();

        requestRepository.findRequestByRequestId(requestId);
        requestRepository.findRequestByReservation_ReservationID(reservationId);
        requestRepository.findRequestsByStatus(status);
        requestRepository.deleteRequestByRequestId(requestId);
        requestRepository.deleteRequestsByReservation_ReservationID(requestId);

        assertNotNull(reservation10);
        assertEquals(reservation10,request.getReservation());
        assertEquals(description,request.getDescription());


//        Person person = new Person();
//        person.setName(name);
//        person.setAge(age);
//        person.setAddress(address);
//
//        // Save person
//        personRepository.save(person);
//
//        // Read person from database.
//        person = personRepository.findPersonByName(name);
//
//        // Assert that person is not null and has correct attributes.
//        assertNotNull(person);
//        assertEquals(name, person.getName());
//        assertEquals(age, person.getAge());
//        assertEquals(address, person.getAddress());
    }
}
