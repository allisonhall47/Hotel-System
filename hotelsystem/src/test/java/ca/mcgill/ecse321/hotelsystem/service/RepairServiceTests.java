package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.*;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.repository.EmployeeRepository;
import ca.mcgill.ecse321.hotelsystem.repository.RepairRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;

import java.util.List;


@ExtendWith(MockitoExtension.class)
public class RepairServiceTests {

    private static final int VALID_REPAIR_KEY = 123;
    private static final String REPAIR_DESCRIPTION = "Fix the door";
    // Employee mock object data
    private static final String EMAIL = "abc@gmail.com";
    private static final String NAME = "Tom";
    private static final int SALARY = 2500;

    @Mock
    private RepairRepository repairDao;

    @Mock
    private EmployeeRepository employeeDao;

    @InjectMocks
    private RepairService service;

    @BeforeEach
    public void setMockOutput() {
        lenient().when(employeeDao.findEmployeeByEmail(EMAIL)).thenAnswer((InvocationOnMock invocation) -> {
            Employee emp = new Employee(EMAIL, NAME, SALARY, null);
            return emp;
        });

        lenient().when(repairDao.findRepairByRepairId(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(VALID_REPAIR_KEY)) {
                return new Repair(CompletionStatus.Pending, REPAIR_DESCRIPTION, employeeDao.findEmployeeByEmail(EMAIL));
            } else {
                return null;
            }
        });
        lenient().when(repairDao.findAll()).thenAnswer((InvocationOnMock invocation) -> List.of(new Repair(CompletionStatus.Pending, REPAIR_DESCRIPTION, employeeDao.findEmployeeByEmail(EMAIL))));
        lenient().when(repairDao.findRepairsByEmployee_Email(anyString())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(EMAIL)) {
                return List.of(new Repair(CompletionStatus.Pending, REPAIR_DESCRIPTION, employeeDao.findEmployeeByEmail(EMAIL)));
            } else {
                return List.of();
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
        Repair rep = service.createRepair(EMAIL, REPAIR_DESCRIPTION);
        assertNotNull(rep);
        assertEquals(EMAIL, rep.getEmployee().getEmail());
        assertEquals(REPAIR_DESCRIPTION, rep.getDescription());
        assertEquals(CompletionStatus.Pending, rep.getStatus());
    }

    @Test
    public void testCreateInvalidRepair() {
        HRSException ex = assertThrows(HRSException.class, () -> service.createRepair(EMAIL, null));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    public void testReadRepair() {
        Repair rep = service.readRepairById(VALID_REPAIR_KEY);
        assertNotNull(rep);
        assertEquals(REPAIR_DESCRIPTION, rep.getDescription());
        assertEquals(EMAIL, rep.getEmployee().getEmail());
    }

    @Test
    public void testReadInvalidRepair() {
        HRSException ex = assertThrows(HRSException.class, () -> service.readRepairById(VALID_REPAIR_KEY + 1));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    public void testGetAllRepairs() {
        List<Repair> reps = service.getAllRepairs();
        assertEquals(1, reps.size());
        assertEquals(REPAIR_DESCRIPTION, reps.get(0).getDescription());
    }

    @Test
    public void testChangeRepairValidStatus() {
        Repair rep = service.changeRepairStatus(VALID_REPAIR_KEY, CompletionStatus.Done);
        assertNotNull(rep);
        assertEquals(CompletionStatus.Done, rep.getStatus());
    }

    @Test
    public void testChangeRepairInvalidStatus() {
        HRSException ex = assertThrows(HRSException.class, () -> service.changeRepairStatus(VALID_REPAIR_KEY, null));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    public void testChangeInvalidRepairStatus() {
        HRSException ex = assertThrows(HRSException.class, () -> service.changeRepairStatus(VALID_REPAIR_KEY+1, CompletionStatus.InProgress));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    public void testGetRepairsByEmployeeEmail() {
        List<Repair> reps = service.getRepairsByEmployeeEmail(EMAIL);
        assertEquals(1, reps.size());
        assertEquals(REPAIR_DESCRIPTION, reps.get(0).getDescription());
    }

    @Test
    public void testGetRepairsByInvalidEmployeeEmail() {
        HRSException ex = assertThrows(HRSException.class, () -> service.getRepairsByEmployeeEmail(EMAIL + "invalid"));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    public void testGetRepairsByNullEmployeeEmail() {
        HRSException ex = assertThrows(HRSException.class, () -> service.getRepairsByEmployeeEmail(null));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    public void testDeleteInvalidRepair() {
        HRSException ex = assertThrows(HRSException.class, () -> service.deleteRepair(VALID_REPAIR_KEY+ 1));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    public void testChangeRepairEmployee() {
        Repair rep = service.changeRepairAssignedEmployee(VALID_REPAIR_KEY, EMAIL);
        assertNotNull(rep);
        assertEquals(EMAIL, rep.getEmployee().getEmail());
    }

    @Test
    public void testChangeInvalidRepairEmployee() {
        HRSException ex = assertThrows(HRSException.class, () -> service.changeRepairAssignedEmployee(VALID_REPAIR_KEY+ 1, EMAIL));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    public void testChangeRepairInvalidEmployee() {
        HRSException ex = assertThrows(HRSException.class, () -> service.changeRepairAssignedEmployee(VALID_REPAIR_KEY, EMAIL+"invalid"));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }
}
