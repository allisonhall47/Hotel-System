package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.Account;
import ca.mcgill.ecse321.hotelsystem.Model.Customer;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CustomerRepositoryTests {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @AfterEach
    public void clearDatabase() {
        customerRepository.deleteAll();
        accountRepository.deleteAll();
    }
    @Test
    public void testPersistAndLoad(){
        Account account = new Account("bestpassword", "1234 idk street", Date.valueOf("2002-10-23"));
        account = accountRepository.save(account);

        Customer customer = new Customer("bill@gmail.com", "Bill", account);
        customer = customerRepository.save(customer);

        Customer customerRep = customerRepository.findCustomerByEmail("bill@gmail.com");

        assertNotNull(customerRep);
        assertEquals("Bill", customerRep.getName());
        assertEquals(Date.valueOf("2002-10-23"), customerRep.getAccount().getDob());
    }

    @Test
    public void testFindCustomersByName(){
        Account account1 = new Account("bestpassword", "1234 idk street", Date.valueOf("2002-10-23"));
        account1 = accountRepository.save(account1);

        Account account2 = new Account("badpassword", "1234 nvm street", Date.valueOf("2002-12-05"));
        account2 = accountRepository.save(account2);

        Customer customer1 = new Customer("bill@gmail.com", "Bill", account1);
        customer1 = customerRepository.save(customer1);

        Customer customer2 = new Customer("billo@gmail.com", "Bill", null);
        customer2 = customerRepository.save(customer2);

        List<Customer> customers = customerRepository.findCustomersByName("Bill");

        assertEquals(2, customers.size());
        assertEquals("bill@gmail.com", customers.get(0).getEmail());
        assertNull(customers.get(1).getAccount());
    }

    @Test
    public void testFindCustomerByAccountId(){
        Account account1 = new Account("bestpassword", "1234 idk street", Date.valueOf("2002-10-23"));
        account1 = accountRepository.save(account1);

        Customer customer1 = new Customer("bill@gmail.com", "Bill", account1);
        customer1 = customerRepository.save(customer1);

        Customer customerRep = customerRepository.findCustomerByAccount_AccountNumber(account1.getAccountNumber());

        assertNotNull(customerRep);
        assertEquals("Bill", customerRep.getName());
    }

    @Test
    public void testFindAllCustomers(){
        Customer customer1 = new Customer("bill@gmail.com", "Bill", null);
        Customer customer2 = new Customer("jill@gmail.com", "Jill", null);
        Customer customer3 = new Customer("yill@gmail.com", "Yill", null);

        customer1 = customerRepository.save(customer1);
        customer2 = customerRepository.save(customer2);
        customer3 = customerRepository.save(customer3);

        List<Customer> customers = customerRepository.findAll();

        assertEquals(3, customers.size());
    }



    @Test
    @Transactional
    public void testDeleteCustomerByEmail(){
        Account account1 = new Account("bestpassword", "1234 idk street", Date.valueOf("2002-10-23"));
        account1 = accountRepository.save(account1);

        Customer customer1 = new Customer("bill@gmail.com", "Bill", account1);
        customer1 = customerRepository.save(customer1);

        Customer customerRep = customerRepository.findCustomerByEmail("bill@gmail.com");
        assertNotNull(customerRep);

        customerRepository.deleteCustomerByEmail("bill@gmail.com");
        customerRep = customerRepository.findCustomerByEmail("bill@gmail.com");
        assertNull(customerRep);
    }
}
