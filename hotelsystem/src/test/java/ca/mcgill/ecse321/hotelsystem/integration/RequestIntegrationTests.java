package ca.mcgill.ecse321.hotelsystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.mcgill.ecse321.hotelsystem.Model.CompletionStatus;
import ca.mcgill.ecse321.hotelsystem.dto.*;

import ca.mcgill.ecse321.hotelsystem.repository.CustomerRepository;
import ca.mcgill.ecse321.hotelsystem.repository.RequestRepository;
import ca.mcgill.ecse321.hotelsystem.repository.ReservationRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
        ResponseEntity<CustomerResponseDto> customerResponse = client.postForEntity("/customer/create", new CustomerRequestDto("john", CUSTOMER_EMAIL), CustomerResponseDto.class);
        assertEquals(HttpStatus.CREATED, customerResponse.getStatusCode());
        ResponseEntity<ReservationResponseDto> reservationResponse = client.postForEntity("/reservation/new", new ReservationRequestDto(2,LocalDate.of(2023,9,1),LocalDate.of(2023,9,4), CUSTOMER_EMAIL), ReservationResponseDto.class);
        assertEquals(HttpStatus.OK, reservationResponse.getStatusCode());
        assertEquals(2, reservationResponse.getBody().getNumPeople());

        res_id = reservationResponse.getBody().getReservationId();
    }

    @Test
    @Order(3)
    public void testCreateRequest() {
        RequestRequestDto req = new RequestRequestDto(REQ_DESCRIPTION, res_id);
        String url = "/request/new";
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
    @Order(4)
    public void testGetRequest() {
        ResponseEntity<RequestResponseDto> res = client.getForEntity("/request/" + req_id, RequestResponseDto.class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        assertEquals(REQ_DESCRIPTION, res.getBody().getDescription());
        assertEquals(CompletionStatus.Pending, res.getBody().getStatus());
        assertEquals(CUSTOMER_EMAIL, res.getBody().getReservation().getCustomer().getEmail());
    }

    @Test
    @Order(5)
    public void testChangeRequestStatus() {
        ResponseEntity<RequestResponseDto> res = client.postForEntity("/request/status/" + req_id, CompletionStatus.Done, RequestResponseDto.class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        assertEquals(CompletionStatus.Done, res.getBody().getStatus());
    }

    @Test
    @Order(6)
    public void testGetRequestsForReservation() {
        ResponseEntity<List> res = client.getForEntity("/request/reservation/" + res_id, List.class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        List<Map<String, Object>> reqs = res.getBody();
        assertEquals(1, reqs.size());
        assertEquals(REQ_DESCRIPTION, reqs.get(0).get("description"));
        assertEquals(CompletionStatus.Done.toString(), reqs.get(0).get("status"));
        Map<String, Object> reservation = (Map<String, Object>) reqs.get(0).get("reservation");
        Map<String, Object> customer = (Map<String, Object>) reservation.get("customer");
        assertEquals(CUSTOMER_EMAIL, customer.get("email"));
    }

    @Test
    @Order(7)
    public void testCreateInvalidRequestNoReservation() {
        RequestRequestDto req = new RequestRequestDto(REQ_DESCRIPTION, res_id+1);
        String url = "/request/new";
        ResponseEntity<String> res = client.postForEntity(url, req, String.class);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    @Order(8)
    public void testCreateInvalidRequestBadDescription() {
        RequestRequestDto req = new RequestRequestDto("", res_id);
        String url = "/request/new";
        ResponseEntity<String> res = client.postForEntity(url, req, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    @Order(9)
    public void testDeleteRequest() {
        client.delete("/request/" + req_id);
        ResponseEntity<String> res = client.getForEntity("/request/" + req_id, String.class);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());

    }

    @Test
    @Order(9)
    public void testDeleteForReservation() {
        String url = "/request/new";

        RequestRequestDto req1 = new RequestRequestDto(REQ_DESCRIPTION, res_id);
        client.postForEntity(url, req1, RequestResponseDto.class);

        RequestRequestDto req2 = new RequestRequestDto(REQ_DESCRIPTION, res_id);
        client.postForEntity(url, req2, RequestResponseDto.class);

        client.delete("/request/reservation/" + res_id);

        ResponseEntity<List> res = client.getForEntity("/request/reservation/" + res_id, List.class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        List<Map<String, Object>> reqs = res.getBody();
        assertEquals(0, reqs.size());
    }
}

