package ca.mcgill.ecse321.hotelsystem.integration;

import ca.mcgill.ecse321.hotelsystem.Model.Account;
import ca.mcgill.ecse321.hotelsystem.dto.AccountRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.AccountResponseDto;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    private Account account = new Account("Password123", "123 Snow Road", LocalDate.of(1990, 3, 3));

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
    public void testCreateInvalidEmptyCustomer(){
        CustomerRequestDto request = new CustomerRequestDto();
        ResponseEntity<String> response = client.postForEntity("/customer/create", request, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(response.getBody(), "Empty field in the customer.");
    }

    @Test
    @Order(3)
    public void testCreateInvalidDuplicateCustomer(){
        CustomerRequestDto request = new CustomerRequestDto(customerFixture.name, customerFixture.email);
        ResponseEntity<String> response = client.postForEntity("/customer/create", request, String.class);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(response.getBody(), "A user with this email already exists.");
    }

    @Test
    @Order(4)
    public void testCreateInvalidEmailCustomer(){
        CustomerRequestDto request = new CustomerRequestDto(customerFixture.name, "jane@white@gmail.com");
        ResponseEntity<String> response = client.postForEntity("/customer/create", request, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(response.getBody(), "Invalid email address.");
    }

    @Test
    @Order(5)
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
    @Order(6)
    public void testValidUpdateCustomerWithAccount(){
        CustomerRequestDto request = new CustomerRequestDto(customerFixture.getName(), customerFixture.getEmail());
        request.setAccountNumber(account.getAccountNumber());
        HttpEntity<CustomerRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<CustomerResponseDto> response = client.exchange("/customer/update", HttpMethod.PUT, requestEntity, CustomerResponseDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(response.getBody().getName(), customerFixture.getName());
        assertEquals(response.getBody().getEmail(), customerFixture.getEmail());
        assertEquals(response.getBody().getAccountNumber(), account.getAccountNumber());
    }

    @Test
    @Order(7)
    public void testInvalidEmptyUpdateCustomer(){
        CustomerRequestDto request = new CustomerRequestDto();
        HttpEntity<CustomerRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> response = client.exchange("/customer/update", HttpMethod.PUT, requestEntity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(response.getBody(), "Empty field in the customer.");
    }

    @Test
    @Order(8)
    public void testInvalidNotFoundUpdateCustomer(){
        CustomerRequestDto request = new CustomerRequestDto(customerFixture.name, "joewhite@gmail.com");
        HttpEntity<CustomerRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> response = client.exchange("/customer/update", HttpMethod.PUT, requestEntity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(response.getBody(), "Customer not found.");
    }

    @Test
    @Order(9)
    public void testValidGetAccount(){
        ResponseEntity<CustomerResponseDto> response = client.getForEntity("/customer?email=" + customerFixture.getEmail(), CustomerResponseDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(equals(response.getBody(), customerFixture));
    }

    @Test
    @Order(10)
    public void testInvalidGetAccount(){
        String email = "joewhite@gmail.com";
        ResponseEntity<String> response = client.getForEntity("/customer?email="+email, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(response.getBody(), "Customer not found.");
    }

    @Test
    @Order(11)
    public void testValidGetAllCustomers(){
        ResponseEntity<List> response = client.getForEntity("/customers", List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody().size(), 1);
        List<Map<String, Object>> customers = response.getBody();
        assertEquals(customers.get(0).get("name"), customerFixture.getName());
        assertEquals(customers.get(0).get("email"), customerFixture.getEmail());
        assertEquals(customers.get(0).get("accountNumber"), customerFixture.getAccountNumber());
    }

    @Test
    @Order(12)
    public void testInvalidDeleteAccount(){
        String email = "joewhite@gmail.com";
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        ResponseEntity<String> response = client.exchange("/customer/delete/"+email, HttpMethod.DELETE, requestEntity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(response.getBody(), "Customer not found.");
    }

    @Test
    @Order(13)
    public void testValidDeleteAccount(){
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        ResponseEntity<String> response = client.exchange("/customer/delete/" + customerFixture.getEmail(), HttpMethod.DELETE, requestEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseEntity<String> response2 = client.getForEntity("/customer?email=" + customerFixture.getEmail(), String.class);
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
        assertEquals(response2.getBody(),  "Customer not found.");
    }

    @Test
    @Order (14)
    public void testValidCreateCustomerWithAccount(){
        AccountRequestDto a = new AccountRequestDto("Password123", "123 Road", LocalDate.of(1990, 3, 3));
        ResponseEntity<AccountResponseDto> a_response = client.postForEntity("/account/create", a, AccountResponseDto.class);

        CustomerRequestDto request = new CustomerRequestDto(customerFixture.name, customerFixture.email);
        request.setAccountNumber(a_response.getBody().getAccountNumber());

        ResponseEntity<CustomerResponseDto> response = client.postForEntity("/customer/create", request, CustomerResponseDto.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        customerFixture.setAccountNumber(a_response.getBody().getAccountNumber());
        assertTrue(equals(response.getBody(), customerFixture));
        customerFixture.setAccountNumber(response.getBody().getAccountNumber());
    }




    private boolean equals(CustomerResponseDto response, CustomerFixture a){
        boolean b = response.getEmail().equals(a.getEmail());
        b = b & (response.getAccountNumber() == a.getAccountNumber());
        return b & response.getName().equals(a.getName());
    }

}

