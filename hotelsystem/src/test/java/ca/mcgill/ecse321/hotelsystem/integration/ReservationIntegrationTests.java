package ca.mcgill.ecse321.hotelsystem.integration;

import ca.mcgill.ecse321.hotelsystem.Model.CheckInStatus;
import ca.mcgill.ecse321.hotelsystem.dto.CustomerRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.CustomerResponseDto;
import ca.mcgill.ecse321.hotelsystem.dto.ReservationRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.ReservationResponseDto;
import ca.mcgill.ecse321.hotelsystem.repository.ReservationRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReservationIntegrationTests {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TestRestTemplate client;

    private static final String EMAIL = "john@email.com";

    private int reservationId;

    @AfterAll
    public void clearDatabase() {
        reservationRepository.deleteAll();
    }

    @Test
    @Order(0)
    public void testCreateValidReservation() {
        ResponseEntity<CustomerResponseDto> customerResponse = client.postForEntity("/customer/create", new CustomerRequestDto("john", EMAIL), CustomerResponseDto.class);
        ResponseEntity<ReservationResponseDto> reservationResponse = client.postForEntity("/reservation/new", new ReservationRequestDto(2,LocalDate.of(2023,9,1),LocalDate.of(2023,9,4), EMAIL), ReservationResponseDto.class);

        assertEquals(HttpStatus.OK, reservationResponse.getStatusCode());
        assertNotNull(reservationResponse.getBody());
        assertTrue(reservationResponse.getBody().getReservationId() > 0, "Response body should have an ID.");
        assertEquals(reservationResponse.getBody().getCustomer().getEmail(), EMAIL);

        reservationId = reservationResponse.getBody().getReservationId();
    }

    @Test
    @Order(1)
    public void testCreateInValidReservation() {
        ResponseEntity<String> response = client.postForEntity("/reservation/new", new ReservationRequestDto(-2,LocalDate.of(2023,9,1),LocalDate.of(2023,9,4), EMAIL), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(response.getBody(), "invalid integer");
    }

    @Test
    @Order(2)
    public void testGetAllReservations() {
        ResponseEntity<List> reservationResponse = client.getForEntity("/reservation", List.class);

        assertEquals(HttpStatus.OK, reservationResponse.getStatusCode());
        assertEquals(reservationResponse.getBody().size(), 1);

        List<Map<String,Object>> reservation = reservationResponse.getBody();
        assertEquals(reservation.get(0).get("reservationId"), reservationId);
    }

    @Test
    @Order(3)
    public void testGetValidReservationById() {
        ResponseEntity<ReservationResponseDto> response = client.getForEntity("/reservation/"+reservationId, ReservationResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody().getCustomer().getEmail(), EMAIL);
        assertEquals(response.getBody().getReservationId(), reservationId);
    }

    @Test
    @Order(4)
    public void testGetInvalidReservationById() {
        int id = -1;
        ResponseEntity<String> response = client.getForEntity("/reservation/"+id, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("reservation not in the system.", response.getBody());
    }

    @Test
    @Order(5)
    public void getAllReservationsForCustomer() {
        ResponseEntity<List> reservationResponse = client.getForEntity("/reservation/customer/"+EMAIL, List.class);

        assertEquals(HttpStatus.OK, reservationResponse.getStatusCode());
        assertEquals(1, reservationResponse.getBody().size());
        List<Map<String,Object>> reservation =reservationResponse.getBody();
        assertEquals(reservation.get(0).get("reservationId"), reservationId);
    }

    @Test
    @Order(6)
    public void testGetAllNonPaidReservations() {
        ResponseEntity<List> reservationResponse = client.getForEntity("/reservation/not-paid", List.class);

        assertEquals(HttpStatus.OK, reservationResponse.getStatusCode());
        assertEquals(1, reservationResponse.getBody().size());
        List<Map<String,Object>> reservation =reservationResponse.getBody();
        assertEquals(reservation.get(0).get("reservationId"), reservationId);
        assertEquals(reservation.get(0).get("paid"), false);
    }

    @Test
    @Order(7)
    public void testPayReservationValid() {
        ResponseEntity<ReservationResponseDto> response = client.exchange("/reservation/"+reservationId+"?money=5", HttpMethod.PUT,null,ReservationResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().getPaid());
    }

    @Test
    @Order(8)
    public void testCheckInReservationValid() {
        ResponseEntity<ReservationResponseDto> response = client.exchange("/reservation/"+reservationId+"/checkIn", HttpMethod.PUT,null,ReservationResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(CheckInStatus.CheckedIn, response.getBody().getCheckedIn());
    }

    @Test
    @Order(9)
    public void testPayReservationInValid() {
        ResponseEntity<String> response = client.exchange("/reservation/"+reservationId+"?money=5",HttpMethod.PUT, null, String.class);

        //reservation already paid in previous test
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("already paid", response.getBody());
    }

    @Test
    @Order(10)
    public void testDeleteReservation() {
        ResponseEntity<String> response = client.exchange("/reservation/"+reservationId, HttpMethod.DELETE, null, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseEntity<String> response2 = client.getForEntity("/reservation/"+reservationId ,String.class);
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
        assertEquals(response2.getBody(),  "reservation not in the system.");
    }

}
