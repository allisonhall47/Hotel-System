package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.Shift;
import ca.mcgill.ecse321.hotelsystem.Model.Employee;
import ca.mcgill.ecse321.hotelsystem.Model.Account;
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
public class ShiftRepositoryTests {
    @Autowired
    private ShiftRepository shiftRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AccountRepository accountRepository;

    @AfterEach
    public void clearDatabase() {
        shiftRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadWithUniqueId() {
        // Create Shift
        Date shiftDate = Date.valueOf("2023-10-04");
        Time startTime = new Time(7, 0, 0);
        Time endTime = new Time(7, 0, 0);

        Shift shift = new Shift();
        shift.setDate(shiftDate);
        shift.setEndTime(endTime);
        shift.setStartTime(startTime);

        // Create Account
        String password = "harrysmith1234";
        String address = "123 snowy road";
        Date dob = Date.valueOf("1980-04-03");
        Account account = new Account();
        account.setPassword(password);
        account.setAddress(address);
        account.setDob(dob);

        // Create Employee
        String name = "Harry Smith";
        String email = "harrysmith@gmail.com";
        int salary = 40000;
        Employee employee = new Employee();
        employee.setSalary(salary);
        employee.setEmail(email);
        employee.setName(name);
        employee.setAccount(account);

        // Finish making shift
        shift.setEmployee(employee);

        // Save into database
        shiftRepository.save(shift);
        employeeRepository.save(employee);
        accountRepository.save(account);

        // Read from database using Unique ID
        int shiftID = shift.getShiftId();
        int accountID = account.getAccountNumber();
        shift = shiftRepository.findShiftByShiftId(shiftID);
        employee = employeeRepository.findEmployeeByEmail(email);
        account = accountRepository.findAccountByAccountNumber(accountID);

        // Check if correct
        assertNotNull(shift);
        assertNotNull(employee);
        assertNotNull(account);

        assertEquals(shiftDate, shift.getDate());
        assertEquals(startTime, shift.getStartTime());
        assertEquals(endTime, shift.getEndTime());

        assertEquals(password, account.getPassword());
        assertEquals(address, account.getAddress());
        assertEquals(dob, account.getDob());

        assertEquals(name, employee.getName());
        assertEquals(email, employee.getEmail());

        assertEquals(employee, shift.getEmployee());
    }

    @Test
    public void testPersistAndLoadWithDate() {
        // Create Shift
        Date shiftDate = Date.valueOf("2023-10-04");
        Time startTime = new Time(7, 0, 0);
        Time endTime = new Time(12, 0, 0);

        Shift shift = new Shift();
        shift.setDate(shiftDate);
        shift.setEndTime(endTime);
        shift.setStartTime(startTime);

        // Create Account
        String password = "harrysmith1234";
        String address = "123 snowy road";
        Date dob = Date.valueOf("1980-04-03");
        Account account = new Account();
        account.setPassword(password);
        account.setAddress(address);
        account.setDob(dob);

        // Create Employee
        String name = "Harry Smith";
        String email = "harrysmith@gmail.com";
        int salary = 40000;
        Employee employee = new Employee();
        employee.setSalary(salary);
        employee.setEmail(email);
        employee.setName(name);
        employee.setAccount(account);

        // Finish making shift
        shift.setEmployee(employee);

        // Create Shift
        Time startTime2 = new Time(14, 0, 0);
        Time endTime2 = new Time(15, 0, 0);

        Shift shift2 = new Shift();
        shift2.setDate(shiftDate);
        shift2.setEndTime(endTime2);
        shift2.setStartTime(startTime2);
        shift2.setEmployee(employee);

        // Save into database
        shiftRepository.save(shift);
        shiftRepository.save(shift2);
        employeeRepository.save(employee);
        accountRepository.save(account);

        // Read from database using Date
        List<Shift> shifts = shiftRepository.findShiftsByDate(shiftDate);
        employee = employeeRepository.findEmployeeByEmail(email);

        // Check if correct
        assertEquals(2, shifts.size());
        assertEquals(shiftDate, shifts.get(0).getDate());
        assertEquals(startTime, shifts.get(0).getStartTime());
        assertEquals(endTime, shifts.get(0).getEndTime());
        assertEquals(employee, shifts.get(0).getEmployee());
        assertEquals(shiftDate, shifts.get(1).getDate());
        assertEquals(startTime2, shifts.get(1).getStartTime());
        assertEquals(endTime2, shifts.get(1).getEndTime());
        assertEquals(employee, shifts.get(1).getEmployee());
    }

    @Test
    public void testPersistAndLoadWithDateAndStartTime() {
        // Create Shift
        Date shiftDate = Date.valueOf("2023-10-04");
        Time startTime = new Time(7, 0, 0);
        Time endTime = new Time(12, 0, 0);

        Shift shift = new Shift();
        shift.setDate(shiftDate);
        shift.setEndTime(endTime);
        shift.setStartTime(startTime);

        // Create Account
        String password = "harrysmith1234";
        String address = "123 snowy road";
        Date dob = Date.valueOf("1980-04-03");
        Account account = new Account();
        account.setPassword(password);
        account.setAddress(address);
        account.setDob(dob);

        // Create Employee
        String name = "Harry Smith";
        String email = "harrysmith@gmail.com";
        int salary = 40000;
        Employee employee = new Employee();
        employee.setSalary(salary);
        employee.setEmail(email);
        employee.setName(name);
        employee.setAccount(account);

        // Finish making shift
        shift.setEmployee(employee);

        // Create Shift
        Time startTime2 = new Time(14, 0, 0);
        Time endTime2 = new Time(15, 0, 0);

        Shift shift2 = new Shift();
        shift2.setDate(shiftDate);
        shift2.setEndTime(endTime2);
        shift2.setStartTime(startTime2);
        shift2.setEmployee(employee);

        // Save into database
        shiftRepository.save(shift);
        shiftRepository.save(shift2);
        employeeRepository.save(employee);
        accountRepository.save(account);

        // Read from database using Date
        List<Shift> shifts = shiftRepository.findShiftsByDateAndStartTime(shiftDate, startTime);

        // Check if correct
        assertEquals(1, shifts.size());
        assertEquals(shiftDate, shifts.get(0).getDate());
        assertEquals(startTime, shifts.get(0).getStartTime());
    }

    @Test
    public void testPersistAndLoadWithEmployeeEmail() {
        // Create Shift
        Date shiftDate = Date.valueOf("2023-10-04");
        Time startTime = new Time(7, 0, 0);
        Time endTime = new Time(12, 0, 0);

        Shift shift = new Shift();
        shift.setDate(shiftDate);
        shift.setEndTime(endTime);
        shift.setStartTime(startTime);

        // Create Account
        String password = "harrysmith1234";
        String address = "123 snowy road";
        Date dob = Date.valueOf("1980-04-03");
        Account account = new Account();
        account.setPassword(password);
        account.setAddress(address);
        account.setDob(dob);

        // Create Employee
        String name = "Harry Smith";
        String email = "harrysmith@gmail.com";
        int salary = 40000;
        Employee employee = new Employee();
        employee.setSalary(salary);
        employee.setEmail(email);
        employee.setName(name);
        employee.setAccount(account);

        // Finish making shift
        shift.setEmployee(employee);

        // Create Shift
        Time startTime2 = new Time(14, 0, 0);
        Time endTime2 = new Time(15, 0, 0);

        Shift shift2 = new Shift();
        shift2.setDate(shiftDate);
        shift2.setEndTime(endTime2);
        shift2.setStartTime(startTime2);
        shift2.setEmployee(employee);

        // Save into database
        shiftRepository.save(shift);
        shiftRepository.save(shift2);
        employeeRepository.save(employee);
        accountRepository.save(account);

        // Read from database using Date
        List<Shift> shifts = shiftRepository.findShiftsByEmployeeEmail(email);

        // Check if correct
        assertEquals(1, shifts.size());
        assertEquals(shiftDate, shifts.get(0).getDate());
        assertEquals(startTime, shifts.get(1).getDate());
    }

    @Test
    @Transactional
    public void testDeleteShift() {
        // Create Shift
        Date shiftDate = Date.valueOf("2023-10-04");
        Time startTime = new Time(7, 0, 0);
        Time endTime = new Time(12, 0, 0);

        Shift shift = new Shift();
        shift.setDate(shiftDate);
        shift.setEndTime(endTime);
        shift.setStartTime(startTime);

        // Create Account
        String password = "harrysmith1234";
        String address = "123 snowy road";
        Date dob = Date.valueOf("1980-04-03");
        Account account = new Account();
        account.setPassword(password);
        account.setAddress(address);
        account.setDob(dob);

        // Create Employee
        String name = "Harry Smith";
        String email = "harrysmith@gmail.com";
        int salary = 40000;
        Employee employee = new Employee();
        employee.setSalary(salary);
        employee.setEmail(email);
        employee.setName(name);
        employee.setAccount(account);

        // Finish making shift
        shift.setEmployee(employee);

        // Create Shift
        Time startTime2 = new Time(14, 0, 0);
        Time endTime2 = new Time(15, 0, 0);


        // Save into database
        shiftRepository.save(shift);
        employeeRepository.save(employee);
        accountRepository.save(account);

        // Read from database using Date
        int shiftID = shift.getShiftId();
        shift = shiftRepository.findShiftByShiftId(shiftID);
        assertNotNull(shift);
        shiftRepository.deleteShiftByShiftId(shiftID);
        shift = shiftRepository.findShiftByShiftId(shiftID);
        assertEquals(null, shift);
    }
}
