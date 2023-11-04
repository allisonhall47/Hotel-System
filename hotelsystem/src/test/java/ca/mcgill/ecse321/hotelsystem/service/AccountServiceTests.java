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
import java.time.LocalDate;

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
        LocalDate dob = LocalDate.of(1980, 3, 3);
        String address = "435 Snow Hill Road";
        Account a1 = new Account(password, address, dob);

        String password2 = "Safepassword1";
        LocalDate dob2 = LocalDate.of(1990, 3, 3);
        String address2 = "34 Rainbow Road";
        Account a2 = new Account(password, address, dob);

        List<Account> accounts = new ArrayList<>();
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
     * Test getting all accounts when none exist
     */
    @Test
    public void testGetAllEmptyAccounts(){
        List<Account> accounts = new ArrayList<>();
        when(accountRepository.findAll()).thenReturn(accounts);

        HRSException e = assertThrows(HRSException.class, () -> accountService.getAllAccounts());
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "There are no accounts in the system.");
    }

    /**
     * Test creating a valid account
     */
    @Test
    public void testCreateValidAccount(){
        String password = "Password123";
        LocalDate dob = LocalDate.of(1980, 3, 3);
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
     * Test creating an account with an invalid password
     */
    @Test
    public void testCreateInvalidPasswordAccount(){
        String password = "Password";
        LocalDate dob = LocalDate.of(1980, 3, 3);
        String address = "435 Snow Hill Road";
        Account a = new Account(password, address, dob);

        HRSException e = assertThrows(HRSException.class, () -> accountService.createAccount(a));
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(e.getMessage(), "Invalid Password");
    }

    /**
     * Test creating an account with an invalid date of birth
     */
    @Test
    public void testCreateInvalidDoBAccount(){
        String password = "Password123";
        LocalDate dob = LocalDate.of(2040, 3, 3);
        String address = "435 Snow Hill Road";
        Account a = new Account(password, address, dob);

        HRSException e = assertThrows(HRSException.class, () -> accountService.createAccount(a));
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(e.getMessage(), "Invalid date of birth.");
    }

    /**
     * Test getting an account with a valid account number
     */
    @Test
    public void testGetAccountByAccountNumber(){
        String password = "Password123";
        LocalDate dob = LocalDate.of(1980, 3, 3);
        String address = "435 Snow Hill Road";
        Account a = new Account(password, address, dob);

        when(accountRepository.findAccountByAccountNumber(a.getAccountNumber())).thenReturn(a);

        Account output = accountService.getAccountByAccountNumber(a.getAccountNumber());
        assertEquals(output, a);
    }

    /**
     * Test getting an account that does not exist
     */
    @Test
    public void testGetAccountByInvalidNumber(){
        int accountNumber = 1;
        when(accountRepository.findAccountByAccountNumber(accountNumber)).thenReturn(null);

        HRSException e = assertThrows(HRSException.class, () -> accountService.getAccountByAccountNumber(accountNumber));
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "Account not found.");
    }

    /**
     * Test updating an account
     */
    @Test
    public void testValidUpdateAccount(){
        String password = "Password123";
        LocalDate dob = LocalDate.of(1980, 3, 3);
        String address = "435 Snow Hill Road";
        Account a = new Account(password, address, dob);
        when(accountRepository.findAccountByAccountNumber(a.getAccountNumber())).thenReturn(a);

        String password2 = "SaferPassword1";
        Account a2 = new Account(password2, address, dob);

        when(accountRepository.save(a)).thenReturn(a);
        Account output = accountService.updateAccount(a2, a.getAccountNumber());
        assertEquals(output.getPassword(), a2.getPassword());
        assertEquals(output.getAddress(), a2.getAddress());
        assertEquals(output.getAccountNumber(), a.getAccountNumber());
    }

    /**
     * Test updating an account that doesn't exist
     */
    @Test
    public void testMissingUpdateAccount(){
        String password = "Password123";
        LocalDate dob = LocalDate.of(1980, 3, 3);
        String address = "435 Snow Hill Road";
        Account a = new Account(password, address, dob);

        HRSException e = assertThrows(HRSException.class, () -> accountService.updateAccount(a, a.getAccountNumber()));
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "Account not found.");
    }

    /**
     * Test updating an account with invalid info
     */
    @Test
    public void testInvalidInfoUpdateAccount(){
        String password = "Password123";
        LocalDate dob = LocalDate.of(1980, 3, 3);
        String address = "435 Snow Hill Road";
        Account a = new Account(password, address, dob);
        when(accountRepository.findAccountByAccountNumber(a.getAccountNumber())).thenReturn(a);

        String password2 = "SaferPassword";
        Account a2 = new Account(password2, address, dob);

        HRSException e = assertThrows(HRSException.class, () -> accountService.updateAccount(a2, a.getAccountNumber()));
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(e.getMessage(), "Invalid Password");
    }

    /**
     * Test updating an account with invalid info
     */
    @Test
    public void testInvalidInfo2UpdateAccount(){
        String password = "Password123";
        LocalDate dob = LocalDate.of(1980, 3, 3);
        String address = "435 Snow Hill Road";
        Account a = new Account(password, address, dob);
        when(accountRepository.findAccountByAccountNumber(a.getAccountNumber())).thenReturn(a);

        LocalDate dob2 = LocalDate.of(2030, 3, 3);
        Account a2 = new Account(password, address, dob2);

        HRSException e = assertThrows(HRSException.class, () -> accountService.updateAccount(a2, a.getAccountNumber()));
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(e.getMessage(), "Invalid date of birth.");
    }

    /**
     * Test deleting an account with invalid account number
     */
    @Test
    public void testInvalidDeleteAccount(){
        int accountNumber = 1;
        when(accountRepository.findAccountByAccountNumber(accountNumber)).thenReturn(null);

        HRSException e = assertThrows(HRSException.class, () -> accountService.deleteAccount(accountNumber));
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "Account not found.");
    }

//    /**
//     * Test deleting an account
//     */
//    @Test
//    public void testValidDeleteAccount(){
//        String password = "Password123";
//        Date dob = Date.valueOf("1990-03-03");
//        String address = "435 Snow Hill Road";
//
//        Account response = new Account(password, address, dob);
//        when(accountRepository.save(response)).thenReturn(response);
//
//        Account output = accountService.createAccount(response);
//        int accountNumber = output.getAccountNumber();
//
//        when(accountRepository.findAccountByAccountNumber(accountNumber)).thenReturn(output);
//        accountService.deleteAccount(accountNumber);
//        when(accountRepository.findAccountByAccountNumber(accountNumber)).thenReturn(null);
//
//        HRSException e = assertThrows(HRSException.class, () -> accountService.getAccountByAccountNumber(accountNumber));
//        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
//        assertEquals(e.getMessage(), "Account not found.");
//    }

}
