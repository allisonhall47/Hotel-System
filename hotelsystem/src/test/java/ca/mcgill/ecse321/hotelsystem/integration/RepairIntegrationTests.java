package ca.mcgill.ecse321.hotelsystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.mcgill.ecse321.hotelsystem.repository.EmployeeRepository;
import ca.mcgill.ecse321.hotelsystem.repository.RepairRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RepairIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private RepairRepository repairRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        repairRepo.deleteAll();
        employeeRepo.deleteAll();
    }

    @Test
    public void testCreateAndGetRepair() {
        ResponseEntity<> emp = client.postForEntity("/employee")
        testGetPerson(id);
    }

    private int testCreateRepair(int employeeId) {
        ResponseEntity<PersonDto> response = client.postForEntity("/person", new PersonDto("Obi-Wan Kenobi"), PersonDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("Obi-Wan Kenobi", response.getBody().getName(), "Response has correct name");
        assertTrue(response.getBody().getId() > 0, "Response has valid ID");

        return response.getBody().getId();
    }

    private void testGetPerson(int id) {
        ResponseEntity<PersonDto> response = client.getForEntity("/person/" + id, PersonDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("Obi-Wan Kenobi", response.getBody().getName(), "Response has correct name");
        assertTrue(response.getBody().getId() == id, "Response has correct ID");
    }

    @Test
    public void testCreateInvalidPerson() {
        ResponseEntity<String> response = client.postForEntity("/person", new PersonDto("   "), String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    }

    @Test
    public void testGetInvalidPerson() {
        ResponseEntity<String> response = client.getForEntity("/person/" + Integer.MAX_VALUE, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Response has correct status");
        assertEquals("Person not found.", response.getBody(), "Response has correct error message");
    }
}

