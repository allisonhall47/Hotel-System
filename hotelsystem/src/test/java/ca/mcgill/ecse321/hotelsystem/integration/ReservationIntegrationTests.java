package ca.mcgill.ecse321.hotelsystem.integration;

import ca.mcgill.ecse321.hotelsystem.dto.CustomerRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.CustomerResponseDto;
import ca.mcgill.ecse321.hotelsystem.dto.ReservationRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.ReservationResponseDto;
import ca.mcgill.ecse321.hotelsystem.repository.ReservationRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReservationIntegrationTests {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TestRestTemplate client;

    @AfterAll
    public void clearDatabase() {
        reservationRepository.deleteAll();
    }

    @Test
    @Order(0)
    public void testCreateValidReservation() {
        ResponseEntity<CustomerResponseDto> customerResponse = client.postForEntity("/customer/create", new CustomerRequestDto("john", "john@gmail.com"), CustomerResponseDto.class);
        ResponseEntity<ReservationResponseDto> reservationResponse = client.postForEntity("/reservation/customer/"+customerResponse.getBody().getEmail()+"/new", new ReservationRequestDto(2,LocalDate.of(2023,9,1),LocalDate.of(2023,9,4), "john@gmail.com"), ReservationResponseDto.class);

        assertEquals(HttpStatus.OK, reservationResponse.getStatusCode());
        assertNotNull(reservationResponse.getBody());
        assertTrue(reservationResponse.getBody().getReservationId() > 0, "Response body should have an ID.");
        assertEquals(reservationResponse.getBody().getCustomer().getEmail(), "john@gmail.com");
    }

    @Test
    @Order(1)
    public void testCreateInValidReservation() {

    }

    @Test
    @Order(2)
    public void testGetAllReservations() {

    }

}
