package ca.mcgill.ecse321.hotelsystem.integration;


import ca.mcgill.ecse321.hotelsystem.Model.Account;
import ca.mcgill.ecse321.hotelsystem.Model.Employee;
import ca.mcgill.ecse321.hotelsystem.dto.CustomerRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.CustomerResponseDto;
import ca.mcgill.ecse321.hotelsystem.dto.EmployeeRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.EmployeeResponseDto;
import ca.mcgill.ecse321.hotelsystem.repository.AccountRepository;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
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
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;


    @Autowired
    private OwnerRepository ownerRepository;


    @Autowired
    private TestRestTemplate client;


    @BeforeAll
    public void setupTestFixture() {
        this.employeeFixture = new EmployeeIntegrationTests.EmployeeFixture();

        Account savedAccount = accountRepository.save(account);
        // Set the account number in fixture
        this.employeeFixture.setAccountNumber(savedAccount.getAccountNumber());
    }

    @BeforeEach
    public void setupForEachTest() {
        // Clear the repository before each test
        employeeRepository.deleteAll();
        // Re-save the account before each test
        Account savedAccount = accountRepository.save(account);

        this.employeeFixture.setAccountNumber(savedAccount.getAccountNumber());
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

        EmployeeRequestDto request = new EmployeeRequestDto(
                employeeFixture.getName(),
                employeeFixture.getEmail(),
                employeeFixture.getSalary(),
                employeeFixture.getAccountNumber() // saved account's number
        );
        ResponseEntity<EmployeeResponseDto> response = client.postForEntity("/employee/create", request, EmployeeResponseDto.class);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(equals(response.getBody(), employeeFixture));
        assertNotNull(response.getBody().getAccountNumber()); // This ensures the account number is not null
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
        EmployeeRequestDto initialRequest = new EmployeeRequestDto(
                employeeFixture.getName(),
                employeeFixture.getEmail(),
                employeeFixture.getSalary(),
                employeeFixture.getAccountNumber()
        );
        client.postForEntity("/employee/create", initialRequest, EmployeeResponseDto.class);

        // Then, attempt to create another employee with the same email
        EmployeeRequestDto duplicateRequest = new EmployeeRequestDto(
                "Jane Doe", // Use a different name
                employeeFixture.getEmail(), // Same email to trigger the duplicate scenario
                employeeFixture.getSalary(),
                employeeFixture.getAccountNumber()
        );
        ResponseEntity<String> response = client.postForEntity("/employee/create", duplicateRequest, String.class);

        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains("A user with this email already exists."));
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
        EmployeeRequestDto createRequest = new EmployeeRequestDto(
                employeeFixture.getName(),
                employeeFixture.getEmail(),
                employeeFixture.getSalary(),
                employeeFixture.getAccountNumber()
        );
        ResponseEntity<EmployeeResponseDto> createResponse = client.postForEntity("/employee/create", createRequest, EmployeeResponseDto.class);
        Assertions.assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        int existingEmployeeAccountNumber = createResponse.getBody().getAccountNumber(); // account number of the created employee

        // create a request to update the employee's name
        EmployeeRequestDto updateRequest = new EmployeeRequestDto(
                "Austin Bill Grey", // New name for the update
                employeeFixture.getEmail(), // Use the same email
                employeeFixture.getSalary(),
                existingEmployeeAccountNumber // Use the captured account number
        );
        HttpEntity<EmployeeRequestDto> requestEntity = new HttpEntity<>(updateRequest);

        // operation
        ResponseEntity<EmployeeResponseDto> updateResponse = client.exchange(
                "/employee/update",
                HttpMethod.PUT,
                requestEntity,
                EmployeeResponseDto.class
        );

        // Validate the update was successful
        Assertions.assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertNotNull(updateResponse.getBody());
        Assertions.assertEquals("Austin Bill Grey", updateResponse.getBody().getName());
        Assertions.assertEquals(employeeFixture.getEmail(), updateResponse.getBody().getEmail());
        Assertions.assertEquals(employeeFixture.getSalary(), updateResponse.getBody().getSalary());
        // Update the fixture to reflect the new state after the update
        employeeFixture.setName("Austin Bill Grey");
    }

    @Test
    @Order(6)
    public void testValidUpdateEmployeeWithAccount() {
        // Create an employee to update
        EmployeeRequestDto createRequest = new EmployeeRequestDto(
                employeeFixture.getName(),
                employeeFixture.getEmail(),
                employeeFixture.getSalary(),
                employeeFixture.getAccountNumber()
        );
        ResponseEntity<EmployeeResponseDto> createResponse = client.postForEntity("/employee/create", createRequest, EmployeeResponseDto.class);
        Assertions.assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());

        // Update the account number of the created employee
        EmployeeRequestDto updateRequest = new EmployeeRequestDto(
                employeeFixture.getName(),
                employeeFixture.getEmail(),
                employeeFixture.getSalary(),
                account.getAccountNumber() // Assuming this is a new/different account number
        );
        HttpEntity<EmployeeRequestDto> requestEntity = new HttpEntity<>(updateRequest);
        ResponseEntity<EmployeeResponseDto> updateResponse = client.exchange("/employee/update", HttpMethod.PUT, requestEntity, EmployeeResponseDto.class);

        // Assert the update was successful
        Assertions.assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertNotNull(updateResponse.getBody());
        Assertions.assertEquals(employeeFixture.getName(), updateResponse.getBody().getName());
        Assertions.assertEquals(employeeFixture.getEmail(), updateResponse.getBody().getEmail());
        Assertions.assertEquals(employeeFixture.getSalary(), updateResponse.getBody().getSalary());
        Assertions.assertEquals(account.getAccountNumber(), updateResponse.getBody().getAccountNumber());
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

        EmployeeRequestDto createRequest = new EmployeeRequestDto(
                employeeFixture.getName(),
                employeeFixture.getEmail(),
                employeeFixture.getSalary(),
                employeeFixture.getAccountNumber()
        );
        ResponseEntity<EmployeeResponseDto> createResponse = client.postForEntity("/employee/create", createRequest, EmployeeResponseDto.class);
        Assertions.assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());

        // Now retrieve the employee by email
        ResponseEntity<EmployeeResponseDto> getResponse = client.getForEntity("/employee?email=" + employeeFixture.getEmail(), EmployeeResponseDto.class);

        Assertions.assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());

        assertTrue(equals(getResponse.getBody(), employeeFixture));
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
        client.delete("/employees/deleteAll");

        EmployeeRequestDto createRequest = new EmployeeRequestDto(
                employeeFixture.getName(),
                employeeFixture.getEmail(),
                employeeFixture.getSalary(),
                employeeFixture.getAccountNumber()
        );
        client.postForEntity("/employee/create", createRequest, EmployeeResponseDto.class);

        // Now get all employees
        ResponseEntity<List> response = client.getForEntity("/employees", List.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        // Assert that there is exactly one employee
        List<Map<String, Object>> employees = response.getBody();
        Assertions.assertEquals(1, employees.size());

        // Extract the employee details from the response
        Map<String, Object> employee = employees.get(0);
        Assertions.assertEquals(employeeFixture.getName(), employee.get("name"));
        Assertions.assertEquals(employeeFixture.getEmail(), employee.get("email"));
        Assertions.assertEquals(employeeFixture.getSalary(), employee.get("salary"));

        Assertions.assertEquals(employeeFixture.getAccountNumber(), ((Number) employee.get("accountNumber")).intValue());
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
        EmployeeRequestDto createRequest = new EmployeeRequestDto(
                employeeFixture.getName(),
                employeeFixture.getEmail(),
                employeeFixture.getSalary(),
                employeeFixture.getAccountNumber()
        );
        client.postForEntity("/employee/create", createRequest, EmployeeResponseDto.class);

        // Now delete the employee
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        ResponseEntity<String> deleteResponse = client.exchange("/employee/delete/" + employeeFixture.getEmail(), HttpMethod.DELETE, requestEntity, String.class);
        Assertions.assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

        // Attempt to retrieve the employee, expecting them to not be found
        ResponseEntity<String> getResponse = client.getForEntity("/employee?email=" + employeeFixture.getEmail(), String.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
        Assertions.assertTrue(getResponse.getBody().contains("Employee not found."));
    }

    @Test
    @Order(14)
    public void testUpdateEmployeeSalary() {

        EmployeeRequestDto createRequest = new EmployeeRequestDto(
                employeeFixture.getName(),
                employeeFixture.getEmail(),
                employeeFixture.getSalary(),
                employeeFixture.getAccountNumber()
        );
        ResponseEntity<EmployeeResponseDto> createResponse = client.postForEntity("/employee/create", createRequest, EmployeeResponseDto.class);
        Assertions.assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());

        // Define a new salary to update the employee with
        int newSalary = employeeFixture.getSalary() + 10000; // Increase salary by 10,000

        // Create a request to update the employee's salary
        EmployeeRequestDto updateRequest = new EmployeeRequestDto(
                employeeFixture.getName(),
                employeeFixture.getEmail(),
                newSalary, // Set the new salary
                employeeFixture.getAccountNumber() // Use the existing account number
        );
        HttpEntity<EmployeeRequestDto> requestEntity = new HttpEntity<>(updateRequest);

        // Perform the update operation
        ResponseEntity<EmployeeResponseDto> updateResponse = client.exchange(
                "/employee/update",
                HttpMethod.PUT,
                requestEntity,
                EmployeeResponseDto.class
        );

        // Validate the update was successful
        Assertions.assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertNotNull(updateResponse.getBody());
        Assertions.assertEquals(newSalary, updateResponse.getBody().getSalary());

        // retrieve the employee again and check if the salary is updated
        ResponseEntity<EmployeeResponseDto> getResponse = client.getForEntity(
                "/employee?email=" + employeeFixture.getEmail(),
                EmployeeResponseDto.class
        );
        Assertions.assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        Assertions.assertEquals(newSalary, getResponse.getBody().getSalary());
    }

    @Test
    @Order(15)
    public void testCreateEmployeeWithInvalidSalary() {

        int invalidSalary = 0; // or any negative value to represent an invalid salary
        EmployeeRequestDto request = new EmployeeRequestDto(
                employeeFixture.getName(),
                employeeFixture.getEmail(),
                invalidSalary,
                employeeFixture.getAccountNumber()
        );

        ResponseEntity<String> response = client.postForEntity("/employee/create", request, String.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains("Invalid salary amount."));
    }

    @Test
    @Order(16)
    public void testDeleteNonExistentEmployeeShouldReturnNotFoundStatus() {
        // A non-existent email address that we are sure is not in the database
        String nonExistentEmail = "nonexistentemail@example.com";

        // Attempt to delete an employee by a non-existent email address
        ResponseEntity<Void> response = client.exchange(
                "/employee/delete/" + nonExistentEmail,
                HttpMethod.DELETE,
                null,
                Void.class);

        // Check for NOT_FOUND status code
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Employee should not be found.");
    }

    @Test
    @Order(17)
    public void testDeleteNonExistentEmployee() {
        String nonExistentEmail = "noone@nowhere.com";
        HttpEntity<String> requestEntity = new HttpEntity<>(null);

        ResponseEntity<String> response = client.exchange(
                "/employee/delete/" + nonExistentEmail,
                HttpMethod.DELETE,
                requestEntity,
                String.class
        );

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains("Employee not found."));
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

