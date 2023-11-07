package ca.mcgill.ecse321.hotelsystem.integration;


import ca.mcgill.ecse321.hotelsystem.Model.Account;
import ca.mcgill.ecse321.hotelsystem.dto.CustomerRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.CustomerResponseDto;
import ca.mcgill.ecse321.hotelsystem.dto.EmployeeRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.EmployeeResponseDto;
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


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)


public class EmployeeIntegrationTests {


    private class EmployeeFixture {
        private String name = "John Doe";


        private String email = "john.doe@gmail.com";


        private int salary = 50000;


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


        public int getSalary() {
            return salary;
        }


        private void setSalary(int salary) {
            this.salary = salary;
        }


        public int getAccountNumber() {
            return accountNumber;
        }


        public void setAccountNumber(int accountNumber) {
            this.accountNumber = accountNumber;
        }
    }


    private Account account = new Account("Password5", "123 Rich Road", LocalDate.of(2002, 4, 9));


    private EmployeeFixture employeeFixture;


    @Autowired
    private EmployeeRepository employeeRepository;


    @Autowired
    private CustomerRepository customerRepository;


    @Autowired
    private OwnerRepository ownerRepository;


    @Autowired
    private TestRestTemplate client;


    @BeforeAll
    public void setupTestFixture() {
        this.employeeFixture = new EmployeeIntegrationTests.EmployeeFixture();
    }


    @BeforeAll
    @AfterAll
    public void clearDatabase() {
        employeeRepository.deleteAll();
    }


    @Test
    @Order(0)
    public void testGetAllEmptyEmployees() {
        ResponseEntity<String> response = client.getForEntity("/employees", String.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().contains("There are no employees in the system."));
    }


    @Test
    @Order(1)
    public void testCreateValidEmployee() {
        EmployeeRequestDto request = new EmployeeRequestDto(employeeFixture.name, employeeFixture.email, employeeFixture.salary, employeeFixture.accountNumber);
        ResponseEntity<EmployeeResponseDto> response = client.postForEntity("/employee/create", request, EmployeeResponseDto.class);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(equals(response.getBody(), employeeFixture));
        employeeFixture.setAccountNumber(response.getBody().getAccountNumber());
    }


    @Test
    @Order(2)
    public void testCreateInvalidEmptyEmployee() {
        EmployeeRequestDto request = new EmployeeRequestDto();
        ResponseEntity<String> response = client.postForEntity("/employee/create", request, String.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(response.getBody(), "Empty field in the employee.");
    }


    @Test
    @Order(3)
    public void testCreateInvalidDuplicateEmployee() {
        EmployeeRequestDto request = new EmployeeRequestDto(employeeFixture.name, employeeFixture.email, employeeFixture.salary, employeeFixture.accountNumber);
        ResponseEntity<String> response = client.postForEntity("/employee/create", request, String.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assertions.assertEquals(response.getBody(), "A user with this email already exists.");
    }


    @Test
    @Order(4)
    public void testCreateInvalidEmailEmployee() {
        EmployeeRequestDto request = new EmployeeRequestDto(employeeFixture.name, "john@doe@gmail.com", employeeFixture.salary, employeeFixture.accountNumber);
        ResponseEntity<String> response = client.postForEntity("/employee/create", request, String.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(response.getBody(), "Invalid email address.");
    }

    @Test
    @Order(5)
    public void testValidUpdateEmployee() {
        EmployeeRequestDto request = new EmployeeRequestDto("Austin Bill Grey", employeeFixture.getEmail(), employeeFixture.getSalary(), employeeFixture.getAccountNumber());
        HttpEntity<EmployeeRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<EmployeeResponseDto> response = client.exchange("/employee/update", HttpMethod.PUT, requestEntity, EmployeeResponseDto.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getName(), "Austin Bill Grey");
        Assertions.assertEquals(response.getBody().getEmail(), employeeFixture.getEmail());
        employeeFixture.setName(response.getBody().getName());
    }

    @Test
    @Order(6)
    public void testValidUpdateEmployeeWithAccount() {
        EmployeeRequestDto request = new EmployeeRequestDto(employeeFixture.getName(), employeeFixture.getEmail(), employeeFixture.getSalary(), employeeFixture.getAccountNumber());
        request.setAccountNumber(account.getAccountNumber());
        HttpEntity<EmployeeRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<EmployeeResponseDto> response = client.exchange("/employee/update", HttpMethod.PUT, requestEntity, EmployeeResponseDto.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getName(), employeeFixture.getName());
        Assertions.assertEquals(response.getBody().getEmail(), employeeFixture.getEmail());
        Assertions.assertEquals(response.getBody().getAccountNumber(), account.getAccountNumber());
    }

    @Test
    @Order(7)
    public void testInvalidEmptyUpdateEmployee() {
        EmployeeRequestDto request = new EmployeeRequestDto();
        HttpEntity<EmployeeRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> response = client.exchange("/employee/update", HttpMethod.PUT, requestEntity, String.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(8)
    public void testInvalidNotFoundUpdateEmployee() {
        EmployeeRequestDto request = new EmployeeRequestDto(employeeFixture.name, "joewhite@gmail.com", employeeFixture.salary, employeeFixture.accountNumber);
        HttpEntity<EmployeeRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> response = client.exchange("/employee/update", HttpMethod.PUT, requestEntity, String.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(response.getBody(), "Employee not found.");
    }


    @Test
    @Order(9)
    public void testValidGetAccount() {
        ResponseEntity<EmployeeResponseDto> response = client.getForEntity("/employee?email=" + employeeFixture.getEmail(), EmployeeResponseDto.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(equals(response.getBody(), employeeFixture));
    }

    @Test
    @Order(10)
    public void testInvalidGetAccount() {
        String email = "alicewonder@gmail.com";
        ResponseEntity<String> response = client.getForEntity("/employee?email=" + email, String.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(response.getBody(), "Employee not found.");
    }

    @Test
    @Order(11)
    public void testValidGetAllEmployees() {
        ResponseEntity<List> response = client.getForEntity("/employees", List.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(response.getBody().size(), 1);
        List<Map<String, Object>> employees = response.getBody();
        Assertions.assertEquals(employees.get(0).get("name"), employeeFixture.getName());
        Assertions.assertEquals(employees.get(0).get("email"), employeeFixture.getEmail());
        Assertions.assertEquals(employees.get(0).get("accountNumber"), employeeFixture.getAccountNumber());
    }

    @Test
    @Order(12)
    public void testInvalidDeleteAccount() {
        String email = "alicewonder@gmail.com";
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        ResponseEntity<String> response = client.exchange("/employee/delete/" + email, HttpMethod.DELETE, requestEntity, String.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(response.getBody(), "Employee not found.");
    }

    @Test
    @Order(13)
    public void testValidDeleteAccount() {
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        ResponseEntity<String> response = client.exchange("/employee/delete/" + employeeFixture.getEmail(), HttpMethod.DELETE, requestEntity, String.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseEntity<String> response2 = client.getForEntity("/employee?email=" + employeeFixture.getEmail(), String.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
        Assertions.assertEquals(response2.getBody(), "Employee not found.");

    }
    private boolean equals (EmployeeResponseDto response, EmployeeFixture fixture){
        if (response == null || fixture == null) {
            return false;
        }
        return response.getName().equals(fixture.getName())
                && response.getEmail().equals(fixture.getEmail())
                && response.getSalary() == fixture.getSalary()
                && response.getAccountNumber() == fixture.getAccountNumber();
    }
}

