package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.Account;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AccountServiceTests {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;


    /**
     * Test get all accounts in the hotel system
     */
    @Test
    public void testGetAllAccounts(){
        String password = "Password123";
        Date dob = Date.valueOf("1990-03-03");
        String address = "435 Snow Hill Road";
        Account a1 = new Account(password, address, dob);

        String password2 = "Safepassword1";
        Date dob2 = Date.valueOf("1995-03-03");
        String address2 = "34 Rainbow Road";
        Account a2 = new Account(password, address, dob);

        List<Account> accounts = new ArrayList<Account>();
        accounts.add(a1);
        accounts.add(a2);

        when(accountRepository.findAll()).thenReturn(accounts);
        List<Account> output = accountService.getAllAccounts();

        assertEquals(2, output.size());
        Iterator<Account> accountsIterator = accounts.iterator();
        assertEquals(a1, accountsIterator.next());
        assertEquals(a2, accountsIterator.next());
    }

    /**
     * Test creating a valid account
     */
    @Test
    public void testCreateValidAccount(){
        String password = "Password123";
        Date dob = Date.valueOf("1990-03-03");
        String address = "435 Snow Hill Road";

        Account response = new Account(password, address, dob);
        when(accountRepository.save(response)).thenReturn(response);

        Account output = accountService.createAccount(response);

        assertNotNull(output);
        assertEquals(response, output);
        verify(accountRepository, times(1)).save(response);
    }

    /**
     * Test creating an invalid account with an empty field
     */
    @Test
    public void testCreateInvalidEmptyAccount(){
        Account a = new Account();
        HRSException e = assertThrows(HRSException.class, () -> accountService.createAccount(a));
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(e.getMessage(), "Empty field in the account");
    }

    /**
     * Test creating an invalid account with an invalid password
     */
    @Test
    public void testCreateInvalidPasswordAccount(){
        String password = "Password";
        Date dob = Date.valueOf("1990-03-03");
        String address = "435 Snow Hill Road";

        Account a = new Account(password, address, dob);
        HRSException e = assertThrows(HRSException.class, () -> accountService.createAccount(a));
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(e.getMessage(), "Invalid Password");
    }

}
