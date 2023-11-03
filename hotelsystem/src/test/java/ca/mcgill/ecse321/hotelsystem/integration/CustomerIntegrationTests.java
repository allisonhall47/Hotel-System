package ca.mcgill.ecse321.hotelsystem.integration;

import ca.mcgill.ecse321.hotelsystem.Model.Account;
import ca.mcgill.ecse321.hotelsystem.dto.CustomerRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.CustomerResponseDto;
import ca.mcgill.ecse321.hotelsystem.repository.CustomerRepository;
import ca.mcgill.ecse321.hotelsystem.repository.EmployeeRepository;
import ca.mcgill.ecse321.hotelsystem.repository.OwnerRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerIntegrationTests {

    private class CustomerFixture {
        private String name = "Jane White";

        private String email = "jane.white@gmail.com";

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

    private CustomerFixture customerFixture;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private TestRestTemplate client;

    @BeforeAll
    public void setupTestFixture(){
        this.customerFixture = new CustomerFixture();
    }

    @BeforeAll
    @AfterAll
    public void clearDatabase(){
        customerRepository.deleteAll();
    }

    @Test
    @Order(0)
    public void testGetAllEmptyCustomers(){
        ResponseEntity<String> response = client.getForEntity("/customers", String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(response.getBody(), "There are no customers in the system.");
    }

    @Test
    @Order(1)
    public void testCreateValidCustomer(){
        CustomerRequestDto request = new CustomerRequestDto(customerFixture.name, customerFixture.email);
        ResponseEntity<CustomerResponseDto> response = client.postForEntity("/customer/create", request, CustomerResponseDto.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(equals(response.getBody(), customerFixture));
        customerFixture.setAccountNumber(response.getBody().getAccountNumber());
    }

    @Test
    @Order(2)
    public void testValidUpdateCustomer(){
        CustomerRequestDto request = new CustomerRequestDto("Jane Mary White", customerFixture.getEmail());
        HttpEntity<CustomerRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<CustomerResponseDto> response = client.exchange("/customer/update", HttpMethod.PUT, requestEntity, CustomerResponseDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(response.getBody().getName(), "Jane Mary White");
        assertEquals(response.getBody().getEmail(), customerFixture.getEmail());
        customerFixture.setName(response.getBody().getName());
    }

    @Test
    @Order(3)
    public void testValidGetAccount(){
        ResponseEntity<CustomerResponseDto> response = client.getForEntity("/customer?email=" + customerFixture.getEmail(), CustomerResponseDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(equals(response.getBody(), customerFixture));
    }

    @Test
    @Order(4)
    public void testValidDeleteAccount(){
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        ResponseEntity<String> response = client.exchange("/customer/delete/" + customerFixture.getEmail(), HttpMethod.DELETE, requestEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseEntity<String> response2 = client.getForEntity("/customer?email=" + customerFixture.getEmail(), String.class);
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
        assertEquals(response2.getBody(),  "Customer not found.");
    }

    private boolean equals(CustomerResponseDto response, CustomerFixture a){
        boolean b = response.getEmail().equals(a.getEmail());
        b = b & (response.getAccountNumber() == a.getAccountNumber());
        return b & response.getName().equals(a.getName());
    }

}

