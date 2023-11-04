package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.Account;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


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
        LocalDate dob = LocalDate.of(2025, 3, 3);
        String address = "123 Drury Lane";
        String password = "hallowelt";

        Account account = new Account();
        account.setAddress(address);
        account.setDob(dob);
        account.setPassword(password);
        // Save account
        account = accountRepository.save(account);

        // Read account from database.
        account = accountRepository.findAccountByAccountNumber(account.getAccountNumber());

        // Assert that account is not null and has correct attributes.
        assertNotNull(account);
        assertEquals(dob, account.getDob());
        assertEquals(address, account.getAddress());
        assertEquals(password, account.getPassword());

    }

    @Test
    @Transactional
    public void testAddAndDeleteMultipleAccounts() {
        String acc1PW = "1";
        String acc2PW = "2";
        String acc3PW = "3";

        Account acc1 = new Account();
        acc1.setPassword(acc1PW);
        Account acc2 = new Account();
        acc2.setPassword(acc2PW);
        Account acc3 = new Account();
        acc3.setPassword(acc3PW);

        acc1 = accountRepository.save(acc1);
        acc2 = accountRepository.save(acc2);
        acc3 = accountRepository.save(acc3);

        accountRepository.deleteAccountByAccountNumber(acc2.getAccountNumber());
        // Now only acc1 and acc3 should be left in the db
        List<Account> results = new ArrayList<>();
        accountRepository.findAll().forEach(results::add);

        assertEquals(2, results.size());

        // The following tests are to verify account is not null
        // Also to verify the getters and setters are working as expected
        acc1 = accountRepository.findAccountByAccountNumber(acc1.getAccountNumber());
        assertNotNull(acc1);
        assertEquals(acc1PW, acc1.getPassword());

        acc3 = accountRepository.findAccountByAccountNumber(acc3.getAccountNumber());
        assertNotNull(acc3);
        assertEquals(acc3PW, acc3.getPassword());


        accountRepository.deleteAll();
        // Now no account should be left in the db
        assertFalse(accountRepository.findAll().iterator().hasNext(), "Accounts table should be empty");
    }
}