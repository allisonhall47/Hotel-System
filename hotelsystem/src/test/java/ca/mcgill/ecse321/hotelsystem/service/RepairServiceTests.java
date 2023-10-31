package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.Account;
import ca.mcgill.ecse321.hotelsystem.Model.CompletionStatus;
import ca.mcgill.ecse321.hotelsystem.Model.Employee;
import ca.mcgill.ecse321.hotelsystem.Model.Repair;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.repository.EmployeeRepository;
import ca.mcgill.ecse321.hotelsystem.repository.RepairRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;


@ExtendWith(MockitoExtension.class)
public class RepairServiceTests {

    private static final int VALID_REPAIR_KEY = 123;
    private static final String REPAIR_DESCRIPTION = "Fix the door";
    // Employee mock object data
    private static final String EMAIL = "abc@gmail.com";
    private static final String NAME = "Tom";
    private static final int SALARY = 2500;
    private static final int ACC_ID = 432;

    @Mock
    private RepairRepository repairDao;

    @Mock
    private EmployeeRepository employeeDao;

    @InjectMocks
    private RepairService service;

    @BeforeEach
    public void setMockOutput() {
        lenient().when(employeeDao.findById(anyString())).thenAnswer((InvocationOnMock invocation) -> {
            Employee emp = new Employee(EMAIL, NAME, SALARY, new Account());
            emp.getAccount().setAccountNumber(ACC_ID);
            return emp;
        });

        lenient().when(repairDao.findRepairByRepairId(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(VALID_REPAIR_KEY)) {
                return new Repair(CompletionStatus.Pending, REPAIR_DESCRIPTION, employeeDao.findEmployeeByEmail(EMAIL));
            } else {
                return null;
            }
        });
        // Whenever anything is saved, just return the parameter object
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
            return invocation.getArgument(0);
        };
        lenient().when(repairDao.save(any(Repair.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(employeeDao.save(any(Employee.class))).thenAnswer(returnParameterAsAnswer);
    }

    @Test
    public void testCreateValidRepair() {
        Repair rep = service.createRepair(ACC_ID, REPAIR_DESCRIPTION);
        assertNotNull(rep);
        assertEquals(EMAIL, rep.getEmployee().getEmail());
        assertEquals(REPAIR_DESCRIPTION, rep.getDescription());
        assertEquals(CompletionStatus.Pending, rep.getStatus());
    }

    @Test
    public void testCreateInvalidRepair() {
        HRSException ex = assertThrows(HRSException.class, () -> service.createRepair(ACC_ID, null));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    public void testReadRepair() {
        Repair rep = service.readRepairById(VALID_REPAIR_KEY);
        assertNotNull(rep);
        assertEquals(REPAIR_DESCRIPTION, rep.getDescription());
        assertEquals(ACC_ID, rep.getEmployee().getAccount().getAccountNumber());
    }

    @Test
    public void testChangeRepairValidStatus() {
        Repair rep = service.changeRepairStatus(VALID_REPAIR_KEY, CompletionStatus.Done);
        assertNotNull(rep);
        assertEquals(CompletionStatus.Done, rep.getStatus());
    }

    @Test
    publoic

}
