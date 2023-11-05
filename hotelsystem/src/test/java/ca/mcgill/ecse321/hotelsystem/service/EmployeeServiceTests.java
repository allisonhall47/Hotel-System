package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.Employee;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.repository.CustomerRepository;
import ca.mcgill.ecse321.hotelsystem.repository.EmployeeRepository;
import ca.mcgill.ecse321.hotelsystem.repository.OwnerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private EmployeeService employeeService;

    /**
     * This test verifies that the getAllEmployees method can successfully retrieve
     * a list of all employees from the system when there are some employees present.
     */
    @Test
    public void testGetAllEmployees() {

        String email1 = "john@gmail.com";
        String name1 = "John Doe";
        int salary1 = 50000;

        Employee e1 = new Employee(email1, name1, salary1, null);

        String email2 = "emily@gmail.com";
        String name2 = "Emily Bower";
        int salary2 = 75000;

        Employee e2 = new Employee(email2, name2, salary2, null);

        List<Employee> employees = new ArrayList<>();
        employees.add(e1);
        employees.add(e2);

        when(employeeRepository.findAll()).thenReturn(employees);
        List<Employee> output = employeeService.getAllEmployees();

        assertEquals(2, output.size());
        Iterator<Employee> employeesIterator = employees.iterator();
        assertEquals(e1, employeesIterator.next());
        assertEquals(e2, employeesIterator.next());
    }

    /**
     * This test checks if the getAllEmployees method throws an HRSException
     * when there are no employees in the system.
     */
    @Test
    public void testGetAllEmployees_NoneExist() {
        when(employeeRepository.findAll()).thenReturn(new ArrayList<>());

        HRSException e = assertThrows(HRSException.class, () -> employeeService.getAllEmployees());
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "There are no employees in the system.");
    }

    /**
     * This test ensures that the getEmployeeByEmail method can correctly fetch
     * an employee based on a provided email when that email corresponds to an existing employee.
     */
    @Test
    public void testGetEmployeeByEmail_Valid() {
        Employee e = new Employee("john@email.com", "John Doe", 30000, null);
        when(employeeRepository.findEmployeeByEmail("john@email.com")).thenReturn(e);

        Employee output = employeeService.getEmployeeByEmail("john@email.com");
        assertEquals(output, e);
    }

    /**
     * This test checks if the getEmployeeByEmail method throws an HRSException
     * when the provided email does not match any employee in the system.
     */
    @Test
    public void testGetEmployeeByEmail_NotFound() {
        when(employeeRepository.findEmployeeByEmail("john@email.com")).thenReturn(null);

        HRSException e = assertThrows(HRSException.class, () -> employeeService.getEmployeeByEmail("john@email.com"));
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "Employee not found.");
    }

    /**
     * This test verifies that the createEmployee method can successfully create
     * a new employee when the provided email is not already in use by any existing
     * employee, customer, or owner in the system.
     */
    @Test
    public void testCreateEmployee_Valid() {
        Employee e = new Employee("john@email.com", "John Doe", 30000, null);
        when(employeeRepository.findEmployeeByEmail(e.getEmail())).thenReturn(null);
        when(customerRepository.findCustomerByEmail(e.getEmail())).thenReturn(null);
        when(ownerRepository.findOwnerByEmail(e.getEmail())).thenReturn(null);
        when(employeeRepository.save(e)).thenReturn(e);

        Employee output = employeeService.createEmployee(e);
        assertEquals(output, e);
    }

    /**
     * This test ensures that the createEmployee method throws an HRSException
     * when trying to create an employee using an email that's already in use
     * by another user (employee, customer, or owner) in the system.
     */
    @Test
    public void testCreateEmployee_EmailExists() {
        Employee e = new Employee("john@email.com", "John Doe", 30000, null);
        when(employeeRepository.findEmployeeByEmail(e.getEmail())).thenReturn(e);

        HRSException eThrown = assertThrows(HRSException.class, () -> employeeService.createEmployee(e));
        assertEquals(eThrown.getStatus(), HttpStatus.CONFLICT);
        assertEquals(eThrown.getMessage(), "A user with this email already exists.");
    }

    /**
     * This test checks if the updateEmployeeInformation method can correctly update
     * an existing employee's information based on the provided new info, and the
     * returned updated employee reflects these changes.
     */
    @Test
    public void testUpdateEmployeeInformation_Valid() {
        // Create old and new employee objects
        Employee oldEmployee = new Employee("john@email.com", "John Doe", 30000, null);
        Employee newEmployeeInfo = new Employee("john@email.com", "John Smith", 32000, null);

        // Mock repository methods
        when(employeeRepository.findEmployeeByEmail(oldEmployee.getEmail())).thenReturn(oldEmployee);
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> {
            Employee updatedEmployee = (Employee) invocation.getArguments()[0];
            return updatedEmployee;  // Return the same employee passed into the save() method
        });

        // Execute the update method
        Employee updatedEmployee = employeeService.updateEmployeeInformation(newEmployeeInfo);

        // Assert the updated values
        assertEquals("John Smith", updatedEmployee.getName());
        assertEquals(32000, updatedEmployee.getSalary());
    }


    /**
     * This test ensures that the updateEmployeeInformation method throws an HRSException
     * when trying to update information for an employee that doesn't exist in the system.
     */
    @Test
    public void testUpdateEmployeeInformation_NotFound() {
        Employee newInfo = new Employee("john@email.com", "John Smith", 32000, null);
        when(employeeRepository.findEmployeeByEmail(newInfo.getEmail())).thenReturn(null);

        HRSException e = assertThrows(HRSException.class, () -> employeeService.updateEmployeeInformation(newInfo));
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "Employee not found.");
    }

    /**
     * This test verifies that the deleteEmployee method can successfully delete
     * an employee based on a provided email.
     */
    @Test
    public void testDeleteEmployee_Valid() {
        String email = "john@gmail.com";
        Employee e = new Employee(email, "John Doe", 50000, null);

        when(employeeRepository.findEmployeeByEmail(email)).thenReturn(e);
        // You can also verify that the delete method was called if needed using Mockito's verify() method

        assertDoesNotThrow(() -> employeeService.deleteEmployee(email)); // Ensure no exceptions are thrown
    }

    /**
     * This test checks if the deleteEmployee method throws an HRSException
     * when the provided email does not match any employee in the system.
     */
    @Test
    public void testDeleteEmployee_NotFound() {
        String email = "john@gmail.com";

        when(employeeRepository.findEmployeeByEmail(email)).thenReturn(null);

        HRSException e = assertThrows(HRSException.class, () -> employeeService.deleteEmployee(email));
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "Employee not found.");
    }
}
