package ca.mcgill.ecse321.hotelsystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.mcgill.ecse321.hotelsystem.Model.CompletionStatus;
import ca.mcgill.ecse321.hotelsystem.dto.*;
import ca.mcgill.ecse321.hotelsystem.repository.AccountRepository;
import ca.mcgill.ecse321.hotelsystem.repository.EmployeeRepository;
import ca.mcgill.ecse321.hotelsystem.repository.RepairRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RepairIntegrationTests {

    private static final String DESCRIPTION = "Fix the doorknob";
    private static final String EMAIL = "someguy@hotelhell.com";
    private static final String EMAIL2 = "thenewguy@gmail.com.com";

    private static int rep_id = 0;

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private RepairRepository repairRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private AccountRepository accountRepo;

    @BeforeAll
    @AfterAll
    public void clearDatabase() {
        repairRepo.deleteAll();
        employeeRepo.deleteAll();
        accountRepo.deleteAll();
    }

    @Test
    @Order(1)
    public void createEmployees() {
        AccountRequestDto accReq = new AccountRequestDto("Password123", "17 builder street", LocalDate.parse("1990-10-10"));
        ResponseEntity<AccountResponseDto> accRes = client.postForEntity("/account/create", accReq, AccountResponseDto.class);
        assertEquals(HttpStatus.CREATED, accRes.getStatusCode());
        int accId = accRes.getBody().getAccountNumber();

        EmployeeRequestDto req = new EmployeeRequestDto("Tom Thomson", EMAIL, 100,accId );
        ResponseEntity<EmployeeResponseDto> res = client.postForEntity("/employee/create", req, EmployeeResponseDto.class);
        assertEquals(HttpStatus.CREATED, res.getStatusCode());

        AccountRequestDto accReq2 = new AccountRequestDto("Password1232", "9 baker street", LocalDate.parse("1968-01-01"));
        ResponseEntity<AccountResponseDto> accRes2 = client.postForEntity("/account/create", accReq2, AccountResponseDto.class);
        assertEquals(HttpStatus.CREATED, accRes2.getStatusCode());
        int accId2 = accRes2.getBody().getAccountNumber();

        EmployeeRequestDto req2 = new EmployeeRequestDto("Tom Thomson", EMAIL2, 100,accId2 );
        ResponseEntity<EmployeeResponseDto> res2 = client.postForEntity("/employee/create", req2, EmployeeResponseDto.class);
        assertEquals(HttpStatus.CREATED, res2.getStatusCode());
    }

    @Test
    @Order(2)
    public void testCreateRepair() {
        RepairRequestDto req = new RepairRequestDto(DESCRIPTION, EMAIL);
        String url = "/repair/new";
        ResponseEntity<RepairResponseDto> res = client.postForEntity(url, req, RepairResponseDto.class);
        assertEquals(HttpStatus.CREATED, res.getStatusCode());
        RepairResponseDto resBody = res.getBody();
        assertNotNull(resBody);
        assertEquals(DESCRIPTION, resBody.getDescription());
        assertNotNull(resBody.getEmployee());
        assertEquals(EMAIL, resBody.getEmployee().getEmail());
        rep_id = resBody.getRepairId();
    }

    @Test
    @Order(3)
    public void testGetAllRepairs() {
        ResponseEntity<List> res = client.getForEntity("/repair", List.class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        List<Map<String, Object>> reqs = res.getBody();
        assertEquals(1, reqs.size());
        assertEquals(DESCRIPTION, reqs.get(0).get("description"));
        assertEquals(CompletionStatus.Pending.toString(), reqs.get(0).get("status"));
        Map<String, Object> employee = (Map<String, Object>) reqs.get(0).get("employee");
        assertEquals(EMAIL, employee.get("email"));
    }

    @Test
    @Order(4)
    public void testGetRepair() {
        ResponseEntity<RepairResponseDto> res = client.getForEntity("/repair/" + rep_id, RepairResponseDto.class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        assertEquals(DESCRIPTION, res.getBody().getDescription());
        assertEquals(CompletionStatus.Pending, res.getBody().getStatus());
        assertEquals(EMAIL, res.getBody().getEmployee().getEmail());
    }

    @Test
    @Order(5)
    public void testGetInvalidRepair() {
        ResponseEntity<String> res = client.getForEntity("/repair/" + (rep_id+1), String.class);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    @Order(6)
    public void testChangeRepairStatus() {
        ResponseEntity<RepairResponseDto> res = client.postForEntity("/repair/status/" + rep_id, CompletionStatus.Done, RepairResponseDto.class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        assertEquals(CompletionStatus.Done, res.getBody().getStatus());
    }

    @Test
    @Order(7)
    public void testChangeInvalidRepairStatus() {
        ResponseEntity<String> res = client.postForEntity("/repair/status/" + (rep_id+1), CompletionStatus.Done, String.class);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    @Order(8)
    public void testChangeRepairEmployee() {
        ResponseEntity<RepairResponseDto> res = client.postForEntity("/repair/employee/" + rep_id, EMAIL2, RepairResponseDto.class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        assertEquals(EMAIL2, res.getBody().getEmployee().getEmail());
    }

    @Test
    @Order(9)
    public void testChangeInvalidRepairEmployee() {
        ResponseEntity<String> res = client.postForEntity("/repair/employee/" + (rep_id+1), EMAIL, String.class);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    @Order(10)
    public void testChangeRepairInvalidEmployee() {
        ResponseEntity<String> res = client.postForEntity("/repair/employee/" + rep_id, EMAIL2+"invalid", String.class);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    @Order(11)
    public void testGetRepairsForEmployee() {
        ResponseEntity<List> res = client.getForEntity("/repair/employee/" + EMAIL2, List.class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        List<Map<String, Object>> reqs = res.getBody();
        assertEquals(1, reqs.size());
        assertEquals(DESCRIPTION, reqs.get(0).get("description"));
        assertEquals(CompletionStatus.Done.toString(), reqs.get(0).get("status"));
        Map<String, Object> employee = (Map<String, Object>) reqs.get(0).get("employee");
        assertEquals(EMAIL2, employee.get("email"));
    }

    @Test
    @Order(12)
    public void testGetRepairsForInvalidEmployee() {
        ResponseEntity<String> res = client.getForEntity("/repair/employee/" + EMAIL + "invalid", String.class);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    @Order(13)
    public void testCreateInvalidRepairNoEmployee() {
        RepairRequestDto req = new RepairRequestDto(DESCRIPTION, EMAIL + "thismakesitinvalid");
        String url = "/repair/new";
        ResponseEntity<String> res = client.postForEntity(url, req, String.class);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    @Order(14)
    public void testCreateInvalidRepairBadDescription() {
        RepairRequestDto req = new RepairRequestDto("", EMAIL);
        String url = "/repair/new";
        ResponseEntity<String> res = client.postForEntity(url, req, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    @Order(15)
    public void testDeleteRepair() {
        ResponseEntity<String> response = client.exchange("/repair/"+rep_id, HttpMethod.DELETE, new HttpEntity<>(null), String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseEntity<String> res = client.getForEntity("/repair/" + rep_id, String.class);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    @Order(16)
    public void testDeleteInvalidRepair() {
        ResponseEntity<String> response = client.exchange("/repair/"+rep_id, HttpMethod.DELETE, new HttpEntity<>(null), String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(17)
    public void testCreateInvalidRepairDescriptionTooShort() {
        RepairRequestDto req = new RepairRequestDto("too short", EMAIL);
        String url = "/repair/new";
        ResponseEntity<String> res = client.postForEntity(url, req, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    @Order(18)
    public void testCreateInvalidRepairDescriptionBlanks() {
        RepairRequestDto req = new RepairRequestDto("                    ", EMAIL);
        String url = "/repair/new";
        ResponseEntity<String> res = client.postForEntity(url, req, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    @Order(19)
    public void testCreateInvalidRepairDescriptionNull() {
        RepairRequestDto req = new RepairRequestDto(null, EMAIL);
        String url = "/repair/new";
        ResponseEntity<String> res = client.postForEntity(url, req, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }
}

