package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.Account;
import ca.mcgill.ecse321.hotelsystem.Model.Customer;
import ca.mcgill.ecse321.hotelsystem.Model.Employee;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.repository.AccountRepository;
import ca.mcgill.ecse321.hotelsystem.repository.CustomerRepository;
import ca.mcgill.ecse321.hotelsystem.repository.EmployeeRepository;
import ca.mcgill.ecse321.hotelsystem.repository.OwnerRepository;
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
public class CustomerServiceTests {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private OwnerRepository ownerRepository;


    /**
     * Test get all customers in the hotel system
     */
    @Test
    public void testGetAllCustomers() {
        String name = "Jane White";
        String email = "jane@gmail.com";
        Customer c1 = new Customer(email, name, null);

        String name2 = "Alex Smith";
        String email2 = "alex@gmail.com";
        Customer c2 = new Customer(email2, name2, null);

        List<Customer> customers = new ArrayList<>();
        customers.add(c1);
        customers.add(c2);

        when(customerRepository.findAll()).thenReturn(customers);
        List<Customer> output = customerService.getAllCustomers();

        assertEquals(2, output.size());
        Iterator<Customer> customersIterator = customers.iterator();
        assertEquals(c1, customersIterator.next());
        assertEquals(c2, customersIterator.next());
    }

    /**
     * Test get all customers when none exist
     */
    @Test
    public void testGetAllEmptyAccounts(){
        List<Customer> customers = new ArrayList<>();
        when(customerRepository.findAll()).thenReturn(customers);

        HRSException e = assertThrows(HRSException.class, () -> customerService.getAllCustomers());
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "There are no customers in the system.");
    }

    /**
     * Test creating a valid customer without an account
     */
    @Test
    public void testCreateValidCustomerWithoutAccount(){
        String name = "Jane White";
        String email = "jane@gmail.com";
        Customer c = new Customer(email, name, null);

        when(customerRepository.save(c)).thenReturn(c);
        when(employeeRepository.findEmployeeByEmail(email)).thenReturn(null);
        when(ownerRepository.findOwnerByEmail(email)).thenReturn(null);

        Customer output = customerService.createCustomer(c);
        assertNotNull(output);
        assertEquals(c, output);
        verify(customerRepository, times(1)).save(c);
    }

    /**
     * Test creating a valid customer with an account
     */
    @Test
    public void testCreateValidCustomerWithAccount(){
        String name = "Jane White";
        String email = "jane@gmail.com";
        String password = "Password123";
        Date dob = Date.valueOf("1990-03-03");
        String address = "435 Snow Hill Road";
        Account a = accountService.createAccount(new Account(password, address, dob));
        Customer c = new Customer(email, name, a);

        when(customerRepository.save(c)).thenReturn(c);
        when(employeeRepository.findEmployeeByEmail(email)).thenReturn(null);
        when(ownerRepository.findOwnerByEmail(email)).thenReturn(null);

        Customer output = customerService.createCustomer(c);
        assertNotNull(output);
        assertEquals(c, output);
        verify(customerRepository, times(1)).save(c);
    }

    /**
     * Test creating a customer where the email has been registered before
     */
    @Test
    public void testCreateInvalidCustomer(){
        String name = "Jane White";
        String email = "jane@gmail.com";
        String password = "Password123";
        Date dob = Date.valueOf("1990-03-03");
        String address = "435 Snow Hill Road";
        Account a = accountService.createAccount(new Account(password, address, dob));
        Employee employee = new Employee(email, name, 30000, a);
        when(employeeRepository.findEmployeeByEmail(email)).thenReturn(employee);
        when(ownerRepository.findOwnerByEmail(email)).thenReturn(null);

        String name2 = "Jane Pink";
        Customer c = new Customer(email, name, null);
        HRSException e = assertThrows(HRSException.class, () -> customerService.createCustomer(c));
        assertEquals(e.getStatus(), HttpStatus.CONFLICT);
        assertEquals(e.getMessage(), "A user with this email already exists.");
    }

}
