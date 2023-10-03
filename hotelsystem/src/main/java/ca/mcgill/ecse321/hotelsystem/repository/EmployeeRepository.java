package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.Employee;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee, String> {
    Employee findEmployeeByEmail(String email);
    List<Employee> findEmployeesByName(String name);
    Employee findEmployeeByAccount_AccountNumber(int id);

    void deleteEmployeeByEmail(String email);

    void deleteEmployeeByName(String name);

    void deleteEmployeeByAccount_AccountNumber(int id);

    List<Employee> findAll();
}
