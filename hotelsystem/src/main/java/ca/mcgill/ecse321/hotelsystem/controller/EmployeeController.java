package ca.mcgill.ecse321.hotelsystem.controller;

import ca.mcgill.ecse321.hotelsystem.Model.Employee;
import ca.mcgill.ecse321.hotelsystem.dto.EmployeeRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.EmployeeResponseDto;
import ca.mcgill.ecse321.hotelsystem.service.AccountService;
import ca.mcgill.ecse321.hotelsystem.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins="*")
@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AccountService accountService;

    /**
     * Retrieves all employees.
     *
     * @return a list of all EmployeeResponseDto objects.
     */
    @GetMapping(value = {"/employees", "/employees/"})
    public List<EmployeeResponseDto> getAllEmployees() {
        return employeeService.getAllEmployees().stream()
                .map(EmployeeResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new employee with the provided employee details.
     *
     * @param employeeRequest DTO containing the details for the new employee.
     * @return a ResponseEntity containing the created EmployeeResponseDto and the HTTP status code.
     */
    @PostMapping(value = {"/employee/create", "/employee/create/"})
    public ResponseEntity<EmployeeResponseDto> createEmployee(@RequestBody EmployeeRequestDto employeeRequest) {
        Employee employee;
        if(employeeRequest.getAccountNumber() == 0){
            employee = employeeRequest.toModel(null);
        } else {
            employee = employeeRequest.toModel(accountService.getAccountByAccountNumber(employeeRequest.getAccountNumber()));
        }
        employee = employeeService.createEmployee(employee);
        return new ResponseEntity<>(new EmployeeResponseDto(employee), HttpStatus.CREATED);
    }

    /**
     * Retrieves an employee by their email address.
     *
     * @param email The email address of the employee to retrieve.
     * @return a ResponseEntity containing the EmployeeResponseDto if found, otherwise an HTTP NOT_FOUND status.
     */
    @GetMapping(value = {"/employee", "/employee/"})
    public ResponseEntity<EmployeeResponseDto> getEmployeeByEmail(@RequestParam String email) {
        return new ResponseEntity<EmployeeResponseDto>(new EmployeeResponseDto(employeeService.getEmployeeByEmail(email)), HttpStatus.OK);
    }

    /**
     * Updates an existing employee's information.
     *
     * @param employeeRequest DTO containing the updated details of the employee.
     * @return a ResponseEntity with the updated EmployeeResponseDto if the employee exists, otherwise HTTP NOT_FOUND status.
     */
    @PutMapping(value = {"/employee/update", "/employee/update/"})
    public ResponseEntity<EmployeeResponseDto> updateEmployee(@RequestBody EmployeeRequestDto employeeRequest) {
        Employee currentEmployee = employeeService.getEmployeeByEmail(employeeRequest.getEmail());
        if(currentEmployee == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Assume the updateEmployeeInformation method will handle setting the values.
        Employee updatedEmployee = employeeService.updateEmployeeInformation(employeeRequest.toModel(currentEmployee.getAccount()));
        return new ResponseEntity<>(new EmployeeResponseDto(updatedEmployee), HttpStatus.OK);
    }

    /**
     * DeleteEmployee: delete an employee from the system
     * @param email: email of the employee to delete
     */
    @DeleteMapping(value = {"/employee/delete/{email}"})
    public void deleteEmployee(@PathVariable String email){
        employeeService.deleteEmployee(email);
    }
}
