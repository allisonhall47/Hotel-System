package ca.mcgill.ecse321.hotelsystem.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.mcgill.ecse321.hotelsystem.Model.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;


@SpringBootTest
public class AccountRepositoryTests {
    @Autowired
    private AccountRepository accountRepository;

    @AfterEach
    public void clearDatabase() {
        accountRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadPerson() {
        // Create Account.
        Integer accountNumber = 1234;
        Date dob = Date.valueOf("2023-12-01");
        String address = "123 Drury Lane";
        String password = "hallowelt";

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setAddress(address);
        account.setDob(dob);
        account.setPassword(password);
        // Save account
        accountRepository.save(account);

        // Read account from database.
        account = accountRepository.findAccountByAccountNumber(accountNumber);

        // Assert that account is not null and has correct attributes.
        assertNotNull(account);
        assertEquals(dob, account.getDob());
        assertEquals(address, account.getAddress());
        assertEquals(password, account.getPassword());

    }

    @Test
    public void testAddAndDeleteMultipleAccounts() {
        Integer acc1ID = 1;
        Integer acc2ID = 2;
        Integer acc3ID = 3;

        Account acc1 = new Account();

    }
}