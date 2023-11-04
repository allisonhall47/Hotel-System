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
import java.time.LocalDate;

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
     * Test get customer with valid email
     */
    @Test
    public void testGetValidCustomer(){
        String name = "Jane White";
        String email = "jane@gmail.com";
        Customer c = new Customer(email, name, null);

        when(customerRepository.findCustomerByEmail(email)).thenReturn(c);

        Customer output = customerService.getCustomerByEmail(email);
        assertEquals(output, c);
    }

    /**
     * Test get customer with invalid email
     */
    @Test
    public void testGetInvalidCustomer(){
        String email = "jane@gmail.com";
        when(customerRepository.findCustomerByEmail(email)).thenReturn(null);

        HRSException e = assertThrows(HRSException.class, () -> customerService.getCustomerByEmail(email));
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "Customer not found.");
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
        LocalDate dob = LocalDate.of(1980, 3, 3);
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
        LocalDate dob = LocalDate.of(1980, 3, 3);
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

    /**
     * Test creating a customer with an invalid email
     */
    @Test
    public void testCreateInvalid2Customer(){
        String name = "Jane White";
        String email = "--janewhite@gmail.com123";
        Customer c = new Customer(email, name, null);

        HRSException e = assertThrows(HRSException.class, () -> customerService.createCustomer(c));
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(e.getMessage(), "Invalid email address.");
    }

    /**
     * Test creating a customer with an empty field
     */
    @Test
    public void testCreateInvalidEmptyCustomer(){
        Customer c = new Customer();
        HRSException e = assertThrows(HRSException.class, () -> customerService.createCustomer(c));
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(e.getMessage(), "Empty field in the customer.");
    }

    /**
     * Test update customer with valid information
     */
    @Test
    public void testValidUpdateCustomer(){
        String name = "Jane White";
        String email = "jane@gmail.com";
        Customer c = new Customer(email, name, null);

        when(customerRepository.findCustomerByEmail(email)).thenReturn(c);

        String password = "Password123";
        LocalDate dob = LocalDate.of(1980, 3, 3);
        String address = "435 Snow Hill Road";
        Account a = accountService.createAccount(new Account(password, address, dob));

        Customer c2 = new Customer(email, name, a);

        when(customerRepository.save(c)).thenReturn(c);
        Customer output = customerService.updateCustomerInformation(c2);
        assertEquals(output.getName(), c2.getName());
        assertEquals(output.getEmail(), c2.getEmail());
        assertEquals(output.getAccount(), c2.getAccount());
    }

    /**
     * Test update customer with invalid information - tries to update email
     */
    @Test
    public void testInvalidUpdateCustomer(){
        String name = "Jane White";
        String email = "jane@gmail.com";
        Customer c = new Customer(email, name, null);

        when(customerRepository.findCustomerByEmail(email)).thenReturn(c);

        String email2 = "jane@hotmail.com";
        Customer c2 = new Customer(email2, name, null);

        HRSException e = assertThrows(HRSException.class, () -> customerService.updateCustomerInformation(c2));
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "Customer not found.");
    }

    /**
     * Test deleting a customer with invalid account number
     */
    @Test
    public void testInvalidDelete(){
        String email = "jane@gmail.com";
        when(customerRepository.findCustomerByEmail(email)).thenReturn(null);

        HRSException e = assertThrows(HRSException.class, () -> customerService.deleteCustomer(email));
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "Customer not found.");
    }

}
