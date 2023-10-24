package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.Account;
import ca.mcgill.ecse321.hotelsystem.Model.Employee;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.repository.CustomerRepository;
import ca.mcgill.ecse321.hotelsystem.repository.EmployeeRepository;
import ca.mcgill.ecse321.hotelsystem.repository.OwnerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    OwnerRepository ownerRepository;

    /**
     * GetAllEmployees: service method to fetch all existing employees in the database
     * @return List of employees
     * @throws HRSException if no employees exist in the system
     */
    @Transactional
    public List<Employee> getAllEmployees(){
        List<Employee> employees = employeeRepository.findAll();
        if (employees.size() == 0){
            throw new HRSException(HttpStatus.NOT_FOUND, "There are no employees in the system.");
        }
        return employees;
    }

    /**
     * GetEmployeeByEmail: service number to fetch an existing employee with a specific email
     * @param email: email of the employee
     * @return employee
     * @throws HRSException if the employee does not exist
     */
    @Transactional
    public Employee getEmployeeByEmail(String email){
        Employee employee = employeeRepository.findEmployeeByEmail(email);
        if (employee == null){
            throw new HRSException(HttpStatus.NOT_FOUND, "Employee not found.");
        }
        return employee;
    }

    /**
     * CreateEmployee: service method to create and store an employee in the database
     * @param employee: employee to be created
     * @return created employee
     * @throws HRSException if a user with the email already exist
     */
    @Transactional
    public Employee createEmployee(Employee employee){
        if ((employeeRepository.findEmployeeByEmail(employee.getEmail()) == null) && (customerRepository.findCustomerByEmail(employee.getEmail()) == null) && (ownerRepository.findOwnerByEmail(employee.getEmail()) == null)) {
            return employeeRepository.save(employee);
        } else {
            throw new HRSException(HttpStatus.CONFLICT, "A user with this email already exists.");
        }
    }

    /**
     * UpdateEmployeeInformation: service method to update information in an employee
     * @param newEmployeeInfo: employee with new information
     * @return updated employee
     */
    @Transactional
    public Employee updateEmployeeInformation(Employee newEmployeeInfo){
        Employee employee = getEmployeeByEmail(newEmployeeInfo.getEmail());
        employee.setName(newEmployeeInfo.getName());
        employee.setSalary(newEmployeeInfo.getSalary());
        employee.setAccount(newEmployeeInfo.getAccount());
        return employeeRepository.save(employee);
    }
}
