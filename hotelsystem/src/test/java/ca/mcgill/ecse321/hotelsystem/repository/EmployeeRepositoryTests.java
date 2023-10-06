package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.Account;
import ca.mcgill.ecse321.hotelsystem.Model.Employee;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class EmployeeRepositoryTests {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AccountRepository accountRepository;

    @AfterEach
    public void clearDatabase() {
        employeeRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoad(){
        Account account = new Account("bestpassword", "1234 idk street", Date.valueOf("2002-10-23"));
        account = accountRepository.save(account);

        Employee employee = new Employee("bill@gmail.com", "Bill", 30000, account);
        employee = employeeRepository.save(employee);

        // retrieves employee by email
        Employee employeeRep = employeeRepository.findEmployeeByEmail("bill@gmail.com");

        // Asserts retrieved employee and verifies properties
        assertNotNull(employeeRep);
        assertEquals("Bill", employeeRep.getName());
        assertEquals(Date.valueOf("2002-10-23"), employeeRep.getAccount().getDob());
    }

    @Test
    public void testFindEmployeesByName(){
        Account account = new Account("bestpassword", "1234 idk street", Date.valueOf("2002-10-23"));
        account = accountRepository.save(account);

        Employee employee = new Employee("bill@gmail.com", "Bill", 30000, account);
        employee = employeeRepository.save(employee);

        Account account2 = new Account("badpassword", "1234 nvm street", Date.valueOf("2002-12-05"));
        account2 = accountRepository.save(account2);

        Employee employee2 = new Employee("billo@gmail.com", "Bill", 30000, account2);
        employee2 = employeeRepository.save(employee2);

        // retrieves employees by name
        List<Employee> employees = employeeRepository.findEmployeesByName("Bill");

        // Asserts retrieved employee is not null and verifies properties
        assertEquals(2, employees.size());
        assertEquals("bill@gmail.com", employees.get(0).getEmail());
        assertEquals("1234 nvm street", employees.get(1).getAccount().getAddress());
    }

    @Test
    public void testFindEmployeeByAccountId(){
        Account account = new Account("bestpassword", "1234 idk street", Date.valueOf("2002-10-23"));
        account = accountRepository.save(account);

        Employee employee = new Employee("bill@gmail.com", "Bill", 30000, account);
        employee = employeeRepository.save(employee);

        // retrieves employee by account number
        Employee employeeRep = employeeRepository.findEmployeeByAccount_AccountNumber(account.getAccountNumber());

        // Asserts retrieved employee and verifies properties
        assertNotNull(employeeRep);
        assertEquals("Bill", employeeRep.getName());
    }

    @Test
    public void testFindAllEmployees(){
        Account account1 = new Account("bestpassword", "1234 idk street", Date.valueOf("2002-10-23"));
        Account account2 = new Account("bestpassword", "1234 idk street", Date.valueOf("2002-10-23"));
        Employee employee1 = new Employee("bill@gmail.com", "Bill", 30000, account1);
        Employee employee2 = new Employee("jill@gmail.com", "Jill", 30000, account2);

        account1 = accountRepository.save(account1);
        account2 = accountRepository.save(account2);
        employee1 = employeeRepository.save(employee1);
        employee2 = employeeRepository.save(employee2);

        // finds all employees in the repository
        List<Employee> employees = employeeRepository.findAll();

        // verifies number of employees matches up
        assertEquals(2, employees.size());
    }


    @Test
    @Transactional
    public void testDeleteEmployeeByEmail(){
        Account account = new Account("bestpassword", "1234 idk street", Date.valueOf("2002-10-23"));
        account = accountRepository.save(account);

        Employee employee = new Employee("bill@gmail.com", "Bill", 30000, account);
        employee = employeeRepository.save(employee);

        Employee employeeRep = employeeRepository.findEmployeeByEmail("bill@gmail.com");
        assertNotNull(employeeRep);
        // deletes employee from repository
        employeeRepository.deleteEmployeeByEmail("bill@gmail.com");
        employeeRep = employeeRepository.findEmployeeByEmail("bill@gmail.com");
        assertNull(employeeRep); // asserts that employee is gone from the repo
    }

}