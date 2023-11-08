package ca.mcgill.ecse321.hotelsystem.integration;

import ca.mcgill.ecse321.hotelsystem.Model.Account;
import ca.mcgill.ecse321.hotelsystem.dto.*;
import ca.mcgill.ecse321.hotelsystem.repository.AccountRepository;
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
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TestRestTemplate client;

    @BeforeAll
    public void setupTestFixture() {
        this.ownerFixture = new OwnerIntegrationTests.OwnerFixture();

        Account savedAccount = accountRepository.save(account);
        // Set the account number in fixture
        this.ownerFixture.setAccountNumber(savedAccount.getAccountNumber());
    }

    @BeforeEach
    public void setupForEachTest() {
        // Clear the repository before each test
        ownerRepository.deleteAll();
        // Re-save the account before each test
        Account savedAccount = accountRepository.save(account);

        this.ownerFixture.setAccountNumber(savedAccount.getAccountNumber());
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
        // Create and save an account first
        Account savedAccount = accountRepository.save(new Account("valid_password", "owner_address", LocalDate.now()));
        // Set the account number in fixture to the saved account's number
        ownerFixture.setAccountNumber(savedAccount.getAccountNumber());

        // Create an owner with the saved account number
        OwnerRequestDto initialRequest = new OwnerRequestDto(
                ownerFixture.getName(),
                ownerFixture.getEmail(),
                ownerFixture.getAccountNumber() // Use the saved account's number
        );

        // Post the request to create an owner
        ResponseEntity<OwnerResponseDto> initialResponse = client.postForEntity("/owner/create", initialRequest, OwnerResponseDto.class);
        assertEquals(HttpStatus.CREATED, initialResponse.getStatusCode());

        // create another owner with the same email, which should fail due to the conflict
        OwnerRequestDto duplicateRequest = new OwnerRequestDto(
                "Another Name", // Use a different name
                ownerFixture.getEmail(), // Same email to trigger the duplicate scenario
                ownerFixture.getAccountNumber() // same account number
        );

        // Post the request to create a duplicate owner
        ResponseEntity<String> duplicateResponse = client.postForEntity("/owner/create", duplicateRequest, String.class);

        // Assert that creating a duplicate owner results in a conflict
        assertEquals(HttpStatus.CONFLICT, duplicateResponse.getStatusCode());

        assertTrue(duplicateResponse.getBody().contains("A user with this email already exists."));
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
    public void testValidUpdateOwner() {
        // First, create an owner to update
        OwnerRequestDto createRequest = new OwnerRequestDto(
                ownerFixture.getName(),
                ownerFixture.getEmail(),
                ownerFixture.getAccountNumber()
        );
        ResponseEntity<OwnerResponseDto> createResponse = client.postForEntity("/owner/create", createRequest, OwnerResponseDto.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());

        // Now, create a request to update the owner's name
        OwnerRequestDto updateRequest = new OwnerRequestDto(
                "Ben Ashton Smalls", // New name for the update
                ownerFixture.getEmail(), // Use the same email
                ownerFixture.getAccountNumber() // Use the same account number
        );
        HttpEntity<OwnerRequestDto> requestEntity = new HttpEntity<>(updateRequest);

        // Perform the update operation
        ResponseEntity<OwnerResponseDto> updateResponse = client.exchange(
                "/owner/update",
                HttpMethod.PUT,
                requestEntity,
                OwnerResponseDto.class
        );

        // Validate the update was successful
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertNotNull(updateResponse.getBody());
        assertEquals("Ben Ashton Smalls", updateResponse.getBody().getName());
        assertEquals(ownerFixture.getEmail(), updateResponse.getBody().getEmail());

        // Update the fixture to reflect the new state after the update
        ownerFixture.setName("Ben Ashton Smalls");
    }


    @Test
    @Order(6)
    public void testValidUpdateOwnerWithAccount() {
        // Assume an account is created and saved before this test runs.
        Account savedAccount = accountRepository.save(new Account("valid_password", "123 Owner St", LocalDate.now()));
        ownerFixture.setAccountNumber(savedAccount.getAccountNumber());

        // Create an owner to update
        OwnerRequestDto createRequest = new OwnerRequestDto(
                ownerFixture.getName(),
                ownerFixture.getEmail(),
                ownerFixture.getAccountNumber() // Use the account number from the saved account
        );
        ResponseEntity<OwnerResponseDto> createResponse = client.postForEntity("/owner/create", createRequest, OwnerResponseDto.class);
        Assertions.assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());

        // Update the owner with a new/different account number
        OwnerRequestDto updateRequest = new OwnerRequestDto(
                ownerFixture.getName(),
                ownerFixture.getEmail(),
                savedAccount.getAccountNumber()
        );
        HttpEntity<OwnerRequestDto> requestEntity = new HttpEntity<>(updateRequest);
        ResponseEntity<OwnerResponseDto> updateResponse = client.exchange("/owner/update", HttpMethod.PUT, requestEntity, OwnerResponseDto.class);

        // Assert the update was successful
        Assertions.assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertNotNull(updateResponse.getBody());
        Assertions.assertEquals(ownerFixture.getName(), updateResponse.getBody().getName());
        Assertions.assertEquals(ownerFixture.getEmail(), updateResponse.getBody().getEmail());
        Assertions.assertEquals(savedAccount.getAccountNumber(), updateResponse.getBody().getAccountNumber());
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
    public void testValidGetAccount() {
        // create an account and save it
        Account savedAccount = accountRepository.save(new Account("valid_password", "owner_address", LocalDate.now()));
        // Update the ownerFixture with the saved account's number
        ownerFixture.setAccountNumber(savedAccount.getAccountNumber());

        // Create an owner to be retrieved
        OwnerRequestDto createRequest = new OwnerRequestDto(
                ownerFixture.getName(),
                ownerFixture.getEmail(),
                ownerFixture.getAccountNumber()
        );
        client.postForEntity("/owner/create", createRequest, OwnerResponseDto.class);

        // Use the GET endpoint to retrieve the owner information
        ResponseEntity<OwnerResponseDto> response = client.getForEntity(
                "/owner/email?email=" + ownerFixture.getEmail(),
                OwnerResponseDto.class
        );

        // Assertions to verify the owner was retrieved successfully
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

    private boolean equals (OwnerResponseDto response, OwnerFixture fixture){
        if (response == null || fixture == null) {
            return false;
        }
        return response.getName().equals(fixture.getName())
                && response.getEmail().equals(fixture.getEmail())
                && response.getAccountNumber() == fixture.getAccountNumber();
    }

}
