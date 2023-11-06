package ca.mcgill.ecse321.hotelsystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.mcgill.ecse321.hotelsystem.Model.CompletionStatus;
import ca.mcgill.ecse321.hotelsystem.dto.*;
import ca.mcgill.ecse321.hotelsystem.repository.EmployeeRepository;
import ca.mcgill.ecse321.hotelsystem.repository.RepairRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RepairIntegrationTests {

    private static final String DESCRIPTION = "Fix the doorknob";
    private static final String EMAIL = "someguy@hotelhell.com";
    private static int emp_id = 0;
    private static int rep_id = 0;

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private RepairRepository repairRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    @BeforeAll
    @AfterAll
    public void clearDatabase() {
        repairRepo.deleteAll();
        employeeRepo.deleteAll();
    }

    @Test
    @Order(1)
    public void createEmployee() {
        EmployeeRequestDto req = new EmployeeRequestDto("Tom Thomson", EMAIL, 100,0 )
        ResponseEntity<EmployeeResponseDto> res = client.postForEntity();
        //...
        // set emp_id
    }

    @Test
    @Order(2)
    public void testCreateRepair() {
        RepairRequestDto req = new RepairRequestDto(DESCRIPTION, emp_id);
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
    public void testGetRepair() {
        ResponseEntity<RepairResponseDto> res = client.getForEntity("/repair/" + rep_id, RepairResponseDto.class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        assertEquals(DESCRIPTION, res.getBody().getDescription());
        assertEquals(CompletionStatus.Pending, res.getBody().getStatus());
        assertEquals(EMAIL, res.getBody().getEmployee().getEmail());
    }

    @Test
    @Order(4)
    public void testChangeRepairStatus() {
        ResponseEntity<RepairResponseDto> res = client.postForEntity("/repair/status/" + rep_id, CompletionStatus.Done, RepairResponseDto.class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        assertEquals(CompletionStatus.Done, res.getBody().getStatus());
    }

    @Test
    @Order(5)
    public void testGetRepairsForEmployee() {
        ResponseEntity<List> res = client.getForEntity("/repair/employee/" + EMAIL, List.class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        List<Map<String, Object>> reqs = res.getBody();
        assertEquals(1, reqs.size());
        assertEquals(DESCRIPTION, reqs.get(0).get("description"));
        assertEquals(CompletionStatus.Done.toString(), reqs.get(0).get("status"));
        Map<String, Object> employee = (Map<String, Object>) reqs.get(0).get("employee");
        assertEquals(EMAIL, employee.get("email"));
    }

    @Test
    @Order(6)
    public void testCreateInvalidRepairNoEmployee() {
        RepairRequestDto req = new RepairRequestDto(DESCRIPTION, emp_id+1);
        String url = "/repair/new";
        ResponseEntity<String> res = client.postForEntity(url, req, String.class);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    @Order(7)
    public void testCreateInvalidRepairBadDescrpiton() {
        RepairRequestDto req = new RepairRequestDto("", emp_id);
        String url = "/repair/new";
        ResponseEntity<String> res = client.postForEntity(url, req, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    @Order(8)
    public void testDeleteRepair() {
        client.delete("/repair/" + rep_id);
        ResponseEntity<String> res = client.getForEntity("/repair/" + rep_id, String.class);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());

    }

    @Test
    @Order(9)
    public void testDeleteForEmployee() {
        String url = "/repair/new";

        RepairRequestDto req1 = new RepairRequestDto(DESCRIPTION, emp_id);
        client.postForEntity(url, req1, RepairResponseDto.class);

        RepairRequestDto req2 = new RepairRequestDto(DESCRIPTION, emp_id);
        client.postForEntity(url, req2, RepairResponseDto.class);

        client.delete("/repair/employee/" + emp_id);

        ResponseEntity<List> res = client.getForEntity("/repair/employee/" + emp_id, List.class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        List<Map<String, Object>> reqs = res.getBody();
        assertEquals(0, reqs.size());
    }
}

