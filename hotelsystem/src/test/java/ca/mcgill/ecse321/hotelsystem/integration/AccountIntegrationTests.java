package ca.mcgill.ecse321.hotelsystem.integration;

import ca.mcgill.ecse321.hotelsystem.dto.AccountRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.AccountResponseDto;
import ca.mcgill.ecse321.hotelsystem.repository.AccountRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountIntegrationTests {

    private class AccountFixture {
        public String password = "Password123";

        public LocalDate dob = LocalDate.of(1990, 3, 3);
        public String address = "435 Snow Hill Road";

        public int accountNumber;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public LocalDate getDob() {
            return dob;
        }

        public void setDob(LocalDate dob) {
            this.dob = dob;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setAccountNumber(int accountNumber){
            this.accountNumber = accountNumber;
        }

        public int getAccountNumber(){
            return accountNumber;
        }
    }

    private AccountFixture accountFixture;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TestRestTemplate client;

    @BeforeAll
    public void setupTestFixture(){
        this.accountFixture = new AccountFixture();
    }

    @BeforeAll
    @AfterAll
    public void clearDatabase(){
        accountRepository.deleteAll();
    }

    @Test
    @Order(0)
    public void testGetAllEmptyAccounts(){
        ResponseEntity<String> response = client.getForEntity("/accounts", String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(response.getBody(), "There are no accounts in the system.");
    }

    @Test
    @Order(1)
    public void testCreateValidAccount(){
        AccountRequestDto request = new AccountRequestDto(accountFixture.password, accountFixture.address, accountFixture.dob);
        ResponseEntity<AccountResponseDto> response = client.postForEntity("/account/create", request, AccountResponseDto.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(equals(response.getBody(), accountFixture));
        accountFixture.setAccountNumber(response.getBody().getAccountNumber());
    }

    @Test
    @Order(2)
    public void testCreateInvalidPasswordAccount(){
        AccountRequestDto request = new AccountRequestDto("password", accountFixture.address, accountFixture.dob);
        ResponseEntity<String> response = client.postForEntity("/account/create", request, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(response.getBody(), "Invalid Password");
    }

    @Test
    @Order(3)
    public void testCreateInvalidDoBAccount(){
        AccountRequestDto request = new AccountRequestDto(accountFixture.password, accountFixture.address, LocalDate.of(2030, 3, 3));
        ResponseEntity<String> response = client.postForEntity("/account/create", request, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(response.getBody(), "Invalid date of birth.");
    }

    @Test
    @Order(4)
    public void testCreateInvalidEmptyAccount(){
        AccountRequestDto request = new AccountRequestDto();
        ResponseEntity<String> response = client.postForEntity("/account/create", request, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(response.getBody(), "Empty field in the account");
    }

    @Test
    @Order(5)
    public void testValidUpdateAccount(){
        AccountRequestDto request = new AccountRequestDto("NewPass123", accountFixture.getAddress(), accountFixture.getDob());
        HttpEntity<AccountRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<AccountResponseDto> response = client.exchange("/account/"+accountFixture.getAccountNumber(), HttpMethod.PUT, requestEntity, AccountResponseDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(response.getBody().getPassword(), "NewPass123");
        assertEquals(response.getBody().getAddress(), accountFixture.getAddress());
        accountFixture.setPassword(response.getBody().getPassword());
    }

    @Test
    @Order(6)
    public void testInvalidNoAccountUpdateAccount(){
        int id = -2;
        AccountRequestDto request = new AccountRequestDto("NewPass123", accountFixture.getAddress(), accountFixture.getDob());
        HttpEntity<AccountRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> response = client.exchange("/account/"+id, HttpMethod.PUT, requestEntity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(response.getBody(), "Account not found.");
    }

    @Test
    @Order(7)
    public void testInvalidFieldUpdateAccount(){
        AccountRequestDto request = new AccountRequestDto("NewPass", accountFixture.getAddress(), accountFixture.getDob());
        HttpEntity<AccountRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> response = client.exchange("/account/"+accountFixture.getAccountNumber(), HttpMethod.PUT, requestEntity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(response.getBody(), "Invalid Password");
    }

    @Test
    @Order(8)
    public void testValidGetAccount(){
        ResponseEntity<AccountResponseDto> response = client.getForEntity("/account?accountNumber=" + accountFixture.getAccountNumber(), AccountResponseDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(equals(response.getBody(), accountFixture));
//        assertEquals(response.getBody().getdob(), accountFixture.getDob());
    }

    @Test
    @Order(9)
    public void testInvalidGetAccount(){
        int id = -2;
        ResponseEntity<String> response = client.getForEntity("/account?accountNumber="+id, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(response.getBody(), "Account not found.");
    }

    @Test
    @Order(10)
    public void testValidGetAllAccounts(){
        ResponseEntity<List> response = client.getForEntity("/accounts", List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody().size(), 1);
        List<Map<String, Object>> accounts = response.getBody();

        System.out.println(accountFixture.getDob());
        System.out.println(accounts.get(0).get("dob") instanceof LocalDate);
        System.out.println(accounts.get(0).get("dob") instanceof String);
        assertEquals(accountFixture.getPassword(), accounts.get(0).get("password"));
        assertEquals(accountFixture.getDob().toString(), accounts.get(0).get("dob"));
        assertEquals(accountFixture.getAccountNumber(), accounts.get(0).get("accountNumber"));
        assertEquals(accountFixture.getAddress(), accounts.get(0).get("address"));
    }

    @Test
    @Order(11)
    public void testInvalidDeleteAccount(){
        int id = -2;
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        ResponseEntity<String> response = client.exchange("/account/delete/"+id, HttpMethod.DELETE, requestEntity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(response.getBody(),  "Account not found.");
    }

    @Test
    @Order(12)
    public void testValidDeleteAccount(){
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        ResponseEntity<String> response = client.exchange("/account/delete/" + accountFixture.getAccountNumber(), HttpMethod.DELETE, requestEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseEntity<String> response2 = client.getForEntity("/account?accountNumber=" + accountFixture.getAccountNumber(), String.class);
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
        assertEquals(response2.getBody(),  "Account not found.");
    }


    private boolean equals(AccountResponseDto response, AccountFixture a){
        boolean b = response.getPassword().equals(a.password);
        b = b & response.getdob().equals(a.dob);
        return b & response.getAddress().equals(a.address);
    }

}
