package ca.mcgill.ecse321.hotelsystem.integration;

import ca.mcgill.ecse321.hotelsystem.dto.AccountRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.AccountRequestUpdateDto;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountIntegrationTests {

    private class AccountFixture {
        public String password = "Password123";
        public Date dob = Date.valueOf("1990-03-03");
        public String address = "435 Snow Hill Road";

        public int accountNumber;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Date getDob() {
            return dob;
        }

        public void setDob(Date dob) {
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
        System.out.println(accountFixture.accountNumber);
    }

    @Test
    @Order(2)
    public void testValidUpdateAccount(){
        AccountRequestDto request = new AccountRequestDto("NewPass123", accountFixture.address, accountFixture.dob);
        HttpEntity<AccountRequestDto> requestEntity = new HttpEntity<>(request);
        int id = accountFixture.accountNumber;
        ResponseEntity<AccountResponseDto> response = client.exchange("/account/"+id, HttpMethod.PUT, requestEntity, AccountResponseDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(response.getBody().getPassword(), "NewPass123");
        assertEquals(response.getBody().getAddress(), accountFixture.address);

        accountFixture.setPassword(response.getBody().getPassword());
    }

    @Test
    @Order(3)
    public void testValidGetAccount(){
        ResponseEntity<AccountResponseDto> response = client.getForEntity("/account?accountNumber=" + accountFixture.getAccountNumber(), AccountResponseDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(equals(response.getBody(), accountFixture));
    }

//    @Test
//    @Order(4)
//    public void testValidDeleteAccount(){
//        HttpEntity<String> requestEntity = new HttpEntity<>(null);
//        ResponseEntity<String> response = client.exchange("/account/delete/" + accountFixture.accountNumber, HttpMethod.DELETE, requestEntity, String.class);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        ResponseEntity<String> response2 = client.getForEntity("/account?accountNumber=" + accountFixture.getAccountNumber(), String.class);
//        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
//        assertEquals(response2.getBody(),  "Account does not exist");
//    }

    private boolean equals(AccountResponseDto response, AccountFixture a){
        boolean b = response.getPassword().equals(a.password);
        return b & response.getAddress().equals(a.address);
    }

}
