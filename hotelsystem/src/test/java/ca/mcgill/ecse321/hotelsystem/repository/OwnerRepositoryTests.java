package ca.mcgill.ecse321.hotelsystem.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.mcgill.ecse321.hotelsystem.Model.Account;
import ca.mcgill.ecse321.hotelsystem.Model.Owner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

@SpringBootTest
public class OwnerRepositoryTests {
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private AccountRepository accountRepository;

    @AfterEach
    public void clearDatabase() {
        ownerRepository.deleteAll();
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

        // Create Owner.
        String name = "Dagobert Duck";
        String email = "boss@gmail.com";

        Owner owner = new Owner();
        owner.setAccount(account);
        owner.setEmail(email);
        owner.setName(name);

        // Save Owner
        ownerRepository.save(owner);

        // Read person from database.
        owner = ownerRepository.findOwnerByEmail(email);

        // Assert that person is not null and has correct attributes.
        assertNotNull(owner);
        assertEquals(name, owner.getName());
        assertNotNull(owner.getAccount());
        assertEquals(account.getAccountNumber(), owner.getAccount().getAccountNumber());
    }
}