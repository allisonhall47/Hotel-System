package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.Account;
import ca.mcgill.ecse321.hotelsystem.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AccountServiceTests {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;


    @Test
    public void testCreateValidAccount(){
//        int s = accountService.getAllAccounts().size();
//        assertEquals(0, accountService.getAllAccounts().size());
        String password = "password123";
        Date dob = Date.valueOf("1990-03-03");
        String address = "435 Snow Hill Road";

        Account response = new Account(password, address, dob);
        when(accountRepository.save(response)).thenReturn(response);

        Account output = accountService.createAccount(response);

        assertNotNull(output);
        assertEquals(response, output);
        verify(accountRepository, times(1)).save(response);

    }


}
