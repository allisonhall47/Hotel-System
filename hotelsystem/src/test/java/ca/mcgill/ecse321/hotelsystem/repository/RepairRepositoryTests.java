package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RepairRepositoryTests {
    @Autowired
    private RepairRepository repairRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AccountRepository accountRepository;

    @AfterEach
    public void clearDatabase() {
        repairRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadWithUniqueId() {
        // Create Account
        String password = "harrysmith1234";
        String address = "123 snowy road";
        Date dob = Date.valueOf("1980-04-03");
        Account account = new Account();
        account.setPassword(password);
        account.setAddress(address);
        account.setDob(dob);
        account = accountRepository.save(account);

        // Create Employee
        String name = "Harry Smith";
        String email = "harrysmith@gmail.com";
        int salary = 40000;
        Employee employee = new Employee();
        employee.setSalary(salary);
        employee.setEmail(email);
        employee.setName(name);
        employee.setAccount(account);
        employee = employeeRepository.save(employee);

        // Create Repair
        String description = "Leaking Roof";
        CompletionStatus status = CompletionStatus.Pending;
        Repair repair = new Repair();
        repair.setDescription(description);
        repair.setStatus(status);
        repair.setEmployee(employee);
        repair = repairRepository.save(repair);

        // Read from database using Unique ID
        int repairID = repair.getRepairId();
        repair = repairRepository.findRepairByRepairId(repairID);

        // Check if correct
        assertNotNull(repair);
        assertEquals(description, repair.getDescription());
        assertEquals(repairID, repair.getRepairId());
    }

    @Test
    public void testPersistAndLoadWithEmployeeEmail() {
        // Create Account
        String password = "harrysmith1234";
        String address = "123 snowy road";
        Date dob = Date.valueOf("1980-04-03");
        Account account = new Account();
        account.setPassword(password);
        account.setAddress(address);
        account.setDob(dob);
        account = accountRepository.save(account);

        // Create Employee
        String name = "Harry Smith";
        String email = "harrysmith@gmail.com";
        int salary = 40000;
        Employee employee = new Employee();
        employee.setSalary(salary);
        employee.setEmail(email);
        employee.setName(name);
        employee.setAccount(account);
        employee = employeeRepository.save(employee);

        // Create Repair
        String description = "Leaking Roof";
        CompletionStatus status = CompletionStatus.Pending;
        Repair repair = new Repair();
        repair.setDescription(description);
        repair.setStatus(status);
        repair.setEmployee(employee);
        repair = repairRepository.save(repair);

        // Create Repair
        String description2 = "Broken heating";
        Repair repair2 = new Repair();
        repair2.setDescription(description2);
        repair2.setStatus(status);
        repair2.setEmployee(employee);
        repair2 = repairRepository.save(repair2);

        // Read from database using Date
        List<Repair> repairs = repairRepository.findRepairsByEmployeeEmail(email);

        // Check if correct
        assertEquals(2, repairs.size());
        assertEquals(description, repairs.get(0).getDescription());
        assertEquals(description2, repairs.get(1).getDescription());
    }

    @Test
    @Transactional
    public void testDeleteRepair() {
        // Create Account
        String password = "harrysmith1234";
        String address = "123 snowy road";
        Date dob = Date.valueOf("1980-04-03");
        Account account = new Account();
        account.setPassword(password);
        account.setAddress(address);
        account.setDob(dob);
        account = accountRepository.save(account);

        // Create Employee
        String name = "Harry Smith";
        String email = "harrysmith@gmail.com";
        int salary = 40000;
        Employee employee = new Employee();
        employee.setSalary(salary);
        employee.setEmail(email);
        employee.setName(name);
        employee.setAccount(account);
        employee = employeeRepository.save(employee);

        // Create Repair
        String description = "Leaking Roof";
        CompletionStatus status = CompletionStatus.Pending;
        Repair repair = new Repair();
        repair.setDescription(description);
        repair.setStatus(status);
        repair.setEmployee(employee);
        repair = repairRepository.save(repair);
        int repairId = repair.getRepairId();

        repair = repairRepository.findRepairByRepairId(repairId);
        assertNotNull(repair);
        repairRepository.deleteRepairByRepairId(repairId);
        repair = repairRepository.findRepairByRepairId(repairId);
        assertEquals(null, repair);
    }


}
