package ca.mcgill.ecse321.hotelsystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.mcgill.ecse321.hotelsystem.Model.CheckInStatus;
import ca.mcgill.ecse321.hotelsystem.Model.Customer;
import ca.mcgill.ecse321.hotelsystem.Model.Reservation;
import ca.mcgill.ecse321.hotelsystem.dto.*;

import ca.mcgill.ecse321.hotelsystem.repository.CustomerRepository;
import ca.mcgill.ecse321.hotelsystem.repository.RequestRepository;
import ca.mcgill.ecse321.hotelsystem.repository.ReservationRepository;
import ca.mcgill.ecse321.hotelsystem.service.ReservationService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RequestIntegrationTests {

    private static final String CUSTOMER_EMAIL = "customer@gmail.com";
    private String REQ_DESCRIPTION = "Repair the door of room 309";
    private static int res_id = 0;
    private static int req_id = 0;

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private RequestRepository requestRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private ReservationRepository reservationRepo;

    @BeforeAll
    @AfterAll
    public void clearDatabase() {
        requestRepo.deleteAll();
        reservationRepo.deleteAll();
        customerRepo.deleteAll();
    }

    @Test
    @Order(1)
    public void createCustomerAndReservation() {
        CustomerRequestDto request = new CustomerRequestDto("John", CUSTOMER_EMAIL);
        ResponseEntity<CustomerResponseDto> response = client.postForEntity("/customer/create", request, CustomerResponseDto.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        ReservationRequestDto resReq = new ReservationRequestDto(2, Date.valueOf("2024-04-05"), Date.valueOf("2024-05-05"), CUSTOMER_EMAIL);
        String url = "/reservation/customer/" + CUSTOMER_EMAIL + "/new";
        ResponseEntity<ReservationResponseDto> res = client.postForEntity(url, resReq, ReservationResponseDto.class);
        assertEquals(2, res.getBody().getNumPeople());

        res_id = res.getBody().getReservationId();
    }

    @Test
    @Order(2)
    public void testCreateRepair() {
        RequestRequestDto req = new RequestRequestDto(REQ_DESCRIPTION, res_id);
        String url = "request/new";
        ResponseEntity<RequestResponseDto> res = client.postForEntity(url, req, RequestResponseDto.class);
        assertEquals(HttpStatus.CREATED, res.getStatusCode());
        RequestResponseDto resBody = res.getBody();
        assertNotNull(resBody);
        assertEquals(REQ_DESCRIPTION, resBody.getDescription());
        assertNotNull(resBody.getReservation());
        assertEquals(res_id, resBody.getReservation().getReservationID());
        req_id = resBody.getRequestId();
    }

    @Test
    @Order(3)
    public void testGetRepair() {
        ResponseEntity<RequestResponseDto> res = client.getForEntity("request/" + req_id, RequestResponseDto.class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        assertEquals(REQ_DESCRIPTION, res.getBody().getDescription());
        assertEquals(CUSTOMER_EMAIL, res.getBody().getReservation().getCustomer().getEmail());
    }
}

