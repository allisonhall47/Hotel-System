package ca.mcgill.ecse321.hotelsystem.integration;

import ca.mcgill.ecse321.hotelsystem.Model.Account;
import ca.mcgill.ecse321.hotelsystem.dto.*;
import ca.mcgill.ecse321.hotelsystem.repository.CustomerRepository;
import ca.mcgill.ecse321.hotelsystem.repository.EmployeeRepository;
import ca.mcgill.ecse321.hotelsystem.repository.OwnerRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class OwnerIntegrationTests {

    private class OwnerFixture {
        private String name = "Alice Wonder";

        private String email = "alice.wonder@gmail.com";

        private int accountNumber;

        public String getName() {
            return name;
        }


        public void setName(String name) {
            this.name = name;
        }


        public String getEmail() {
            return email;
        }


        public void setEmail(String email) {
            this.email = email;
        }


        public int getAccountNumber() {
            return accountNumber;
        }


        public void setAccountNumber(int accountNumber) {
            this.accountNumber = accountNumber;
        }

    }

    private Account account = new Account("Pass222", "333 Abs Road", LocalDate.of(2002, 2, 2));

    private OwnerFixture ownerFixture;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TestRestTemplate client;

    @BeforeAll
    public void setupTestFixture(){
        this.ownerFixture = new OwnerFixture();
    }

    @BeforeAll
    @AfterAll
    public void clearDatabase(){
        ownerRepository.deleteAll();
    }

    @Test
    @Order(0)
    public void testGetAllEmptyOwners() {
        ResponseEntity<String> response = client.getForEntity("/owner/", String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(response.getBody(), "There are no owners in the system.");
    }

    @Test
    @Order(1)
    public void testCreateValidOwner(){
        OwnerRequestDto request = new OwnerRequestDto(ownerFixture.name, ownerFixture.email, ownerFixture.accountNumber);
        ResponseEntity<OwnerResponseDto> response = client.postForEntity("/owner/create", request, OwnerResponseDto.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(equals(response.getBody(), ownerFixture));
        ownerFixture.setAccountNumber(response.getBody().getAccountNumber());
    }

    @Test
    @Order(2)
    public void testCreateInvalidEmptyOwner(){
        OwnerRequestDto request = new OwnerRequestDto();
        ResponseEntity<String> response = client.postForEntity("/owner/create", request, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(response.getBody(), "Empty field in the owner.");
    }

    @Test
    @Order(3)
    public void testCreateInvalidDuplicateOwner(){
        OwnerRequestDto request = new OwnerRequestDto(ownerFixture.name, ownerFixture.email, ownerFixture.accountNumber);
        ResponseEntity<String> response = client.postForEntity("/owner/create", request, String.class);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(response.getBody(), "A user with this email already exists.");
    }

    @Test
    @Order(4)
    public void testCreateInvalidEmailOwner(){
        OwnerRequestDto request = new OwnerRequestDto(ownerFixture.name, "alice@wonder@gmail.com", ownerFixture.accountNumber);
        ResponseEntity<String> response = client.postForEntity("/owner/create", request, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(response.getBody(), "Invalid email address.");
    }

    @Test
    @Order(5)
    public void testValidUpdateOwner(){
        OwnerRequestDto request = new OwnerRequestDto("Ben Ashton Smalls", ownerFixture.getEmail(), ownerFixture.getAccountNumber());
        HttpEntity<OwnerRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<OwnerResponseDto> response = client.exchange("/owner/update", HttpMethod.PUT, requestEntity, OwnerResponseDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(response.getBody().getName(), "Ben Ashton Smalls");
        assertEquals(response.getBody().getEmail(), ownerFixture.getEmail());
        ownerFixture.setName(response.getBody().getName());
    }

    @Test
    @Order(6)
    public void testValidUpdateOwnerWithAccount(){
        OwnerRequestDto request = new OwnerRequestDto(ownerFixture.getName(), ownerFixture.getEmail(), ownerFixture.getAccountNumber());
        request.setAccountNumber(account.getAccountNumber());
        HttpEntity<OwnerRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<OwnerResponseDto> response = client.exchange("/owner/update", HttpMethod.PUT, requestEntity, OwnerResponseDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(response.getBody().getName(), ownerFixture.getName());
        assertEquals(response.getBody().getEmail(), ownerFixture.getEmail());
        assertEquals(response.getBody().getAccountNumber(), account.getAccountNumber());
    }

    @Test
    @Order(7)
    public void testInvalidEmptyUpdateOwner() {
        OwnerRequestDto request = new OwnerRequestDto();
        HttpEntity<OwnerRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> response = client.exchange("/owner/update", HttpMethod.PUT, requestEntity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(response.getBody(), "Empty field in the owner.");
    }

    @Test
    @Order(8)
    public void testInvalidNotFoundUpdateOwner(){
        OwnerRequestDto request = new OwnerRequestDto(ownerFixture.name, "alicewonder@gmail.com", ownerFixture.accountNumber);
        HttpEntity<OwnerRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> response = client.exchange("/owner/update", HttpMethod.PUT, requestEntity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(response.getBody(), "Owner not found.");
    }

    @Test
    @Order(9)
    public void testValidGetAccount(){
        ResponseEntity<OwnerResponseDto> response = client.getForEntity("/owner/email?email=" + ownerFixture.getEmail(), OwnerResponseDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(equals(response.getBody(), ownerFixture));
    }

    @Test
    @Order(10)
    public void testInvalidGetAccount(){
        String email = "alicewonder@gmail.com";
        ResponseEntity<String> response = client.getForEntity("/owner/email?email=" + email, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(response.getBody(), "Owner not found.");
    }

    @Test
    @Order(11)
    public void testValidGetAllOwners(){
        ResponseEntity<List> response = client.getForEntity("/owner", List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody().size(), 1);
        List<Map<String, Object>> customers = response.getBody();
        assertEquals(customers.get(0).get("name"), ownerFixture.getName());
        assertEquals(customers.get(0).get("email"), ownerFixture.getEmail());
        assertEquals(customers.get(0).get("accountNumber"), ownerFixture.getAccountNumber());
    }

    private boolean equals (OwnerResponseDto response, OwnerFixture fixture){
        if (response == null || fixture == null) {
            return false;
        }
        return response.getName().equals(fixture.getName())
                && response.getEmail().equals(fixture.getEmail())
                && response.getAccountNumber() == fixture.getAccountNumber();
    }

}
