package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.CompletionStatus;
import ca.mcgill.ecse321.hotelsystem.Model.Repair;
import ca.mcgill.ecse321.hotelsystem.repository.EmployeeRepository;
import ca.mcgill.ecse321.hotelsystem.repository.RepairRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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



@ExtendWith(MockitoExtension.class)
public class RepairServiceTests {

    private static final int VALID_REPAIR_KEY = 123;
    private static final String REPAIR_DESCRIPTION = "Fix the door";

    @Mock
    private RepairRepository repairDao;

    @Mock
    private EmployeeRepository employeeDao;

    @InjectMocks
    private RepairService service;

    @BeforeEach
    public void setMockOutput() {
        lenient().when(repairDao.findRepairByRepairId(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(VALID_REPAIR_KEY)) {
                Repair repair = new Repair(CompletionStatus.Pending, );
                repair.setDescription()
                return person;
            } else {
                return null;
            }
        });
        // Whenever anything is saved, just return the parameter object
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
            return invocation.getArgument(0);
        };
        lenient().when(repairDao.save(any(Repair.class))).thenAnswer(returnParameterAsAnswer);
    }


}
