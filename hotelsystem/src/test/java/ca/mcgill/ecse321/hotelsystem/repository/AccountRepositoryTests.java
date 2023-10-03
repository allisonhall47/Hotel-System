package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Date dob = Date.valueOf("2023-12-01");
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

        List<Account> results = new ArrayList<>();
        accountRepository.findAll().forEach(results::add);

        assertEquals(2, results.size());

        Optional<Account> shouldBeAcc1 = results.stream().filter(acc -> acc1PW.equals(acc.getPassword())).findFirst();
        assertFalse(shouldBeAcc1.isEmpty(), "Account 1 is not present in db");
        assertEquals(acc1.getAccountNumber(), shouldBeAcc1.get().getAccountNumber());

        Optional<Account> shouldBeAcc3 = results.stream().filter(acc -> acc3PW.equals(acc.getPassword())).findFirst();
        assertFalse(shouldBeAcc3.isEmpty(), "Account 3 is not present in db");
        assertEquals(acc3.getAccountNumber(), shouldBeAcc3.get().getAccountNumber());

        accountRepository.deleteAccountByAccountNumber(acc1.getAccountNumber());
        assertEquals(acc3PW, accountRepository.findAccountByAccountNumber(acc3.getAccountNumber()).getPassword());

        accountRepository.deleteAll();
        assertFalse(accountRepository.findAll().iterator().hasNext(), "Accounts table should be empty");
    }
}