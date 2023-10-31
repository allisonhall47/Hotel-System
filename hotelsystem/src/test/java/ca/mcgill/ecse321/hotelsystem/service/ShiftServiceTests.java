package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.Account;
import ca.mcgill.ecse321.hotelsystem.Model.Employee;
import ca.mcgill.ecse321.hotelsystem.Model.Shift;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.repository.ShiftRepository;
import ca.mcgill.ecse321.hotelsystem.repository.EmployeeRepository;
import org.aspectj.apache.bcel.Repository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.sql.Date;
import java.sql.Time;
import java.sql.Wrapper;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ShiftServiceTests {

      @Mock
      private ShiftRepository shiftRepository;

      @Mock
      private EmployeeRepository employeeRepository;
      @InjectMocks
      private ShiftService shiftService;

      @Test
      public void testGetAllShifts() {
            int shiftId1 = 372;
            Time startTime1 = Time.valueOf("6:30:00");
            Time endTime1 = Time.valueOf("12:30:00");
            Date date1 = Date.valueOf("1994-06-15");
            Shift shift1 = new Shift(shiftId1,startTime1,endTime1,date1,null);

            int shiftId2 = 373;
            Time startTime2 = Time.valueOf("12:30:00");
            Time endTime2 = Time.valueOf("18:30:00");
            Date date2 = Date.valueOf("1994-06-15");
            Shift shift2 = new Shift(shiftId2,startTime2,endTime2,date2,null);

            List<Shift> shiftList = new ArrayList<>();
            shiftList.add(shift1);
            shiftList.add(shift2);

            when(shiftRepository.findAll()).thenReturn(shiftList);
            List<Shift> sOutput = shiftService.getAllShifts();

            assertEquals(2, sOutput.size());
            Iterator<Shift> shiftIterator = shiftList.iterator();
            assertEquals(shift1,shiftIterator.next());
            assertEquals(shift2,shiftIterator.next());
      }
      @Test
      public void testGetAllEmptyShifts() {
            List<Shift> shiftList = new ArrayList<>();
            when(shiftRepository.findAll()).thenReturn(shiftList);

            HRSException se = assertThrows(HRSException.class, () -> shiftService.getAllShifts());
            assertEquals(se.getStatus(), HttpStatus.NOT_FOUND);
            assertEquals(se.getMessage(), "There are no shifts in the system.");
      }

      @Test
      public void testGetValidShift() {
            int shiftID = 425;
            Time startTime = Time.valueOf("7:30:00");
            Time endTime = Time.valueOf("14:30:00");
            Date date = Date.valueOf("1993-04-20");
            Shift shift = new Shift(shiftID,startTime,endTime,date,null);

            when(shiftRepository.findShiftByShiftId(shiftID)).thenReturn(shift);

            Shift outputShift = shiftService.getShiftByShiftID(shiftID);
            assertEquals(outputShift, shift);
      }

      @Test
      public void testCreateValidShiftWithoutEmployee() {
            int shiftID = 425;
            Time startTime = Time.valueOf("7:30:00");
            Time endTime = Time.valueOf("14:30:00");
            Date date = Date.valueOf("1993-04-20");
            Shift shift = new Shift(shiftID,startTime,endTime,date,null);

            when(shiftRepository.save(shift)).thenReturn(shift);

            Shift outputShift = shiftService.createShift(shift);
            assertNotNull(outputShift);
            assertEquals(shift,outputShift);
            verify(shiftRepository,times(1)).save(shift);
      }

      /*
       * Test creating a shift with the end time being after the start time
       */
      @Test
      public void testCreateInvalidTimeShift() {
            int shiftID = 430;
            Time startTime = Time.valueOf("7:30:00");
            Time endTime = Time.valueOf("5:30:00");
            Date date = Date.valueOf("1993-04-20");
            Shift shift = new Shift(shiftID,startTime,endTime,date,null);

            HRSException e = assertThrows(HRSException.class, () -> shiftService.createShift(shift));
            assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
            assertEquals(e.getMessage(), "Invalid start/end times.");
      }

      @Test
      public void testCreateInvalidShift() {
            Shift shift = new Shift();
            HRSException e = assertThrows(HRSException.class, () -> shiftService.createShift(shift));
            assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
            assertEquals(e.getMessage(),"Empty fields are present.");
      }

      @Test
      public void testGetShiftByShiftID() {
            int shiftID = 430;
            Time startTime = Time.valueOf("7:30:00");
            Time endTime = Time.valueOf("5:30:00");
            Date date = Date.valueOf("1993-04-20");
            Shift shift = new Shift(shiftID,startTime,endTime,date,null);

            when(shiftRepository.findShiftByShiftId(shiftID)).thenReturn(shift);
            Shift outputShift = shiftService.getShiftByShiftID(shift.getShiftId());
            assertEquals(outputShift,shift);
      }

      @Test
      public void testGetShiftByInvalidShiftID() {
            int shiftID = -430;

            when(shiftRepository.findShiftByShiftId(shiftID)).thenReturn(null);

            HRSException e = assertThrows(HRSException.class, () -> shiftService.getShiftByShiftID(shiftID));
            assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
            assertEquals(e.getMessage(), "Invalid shift ID.");
      }

      @Test
      public void testCreateShiftWithInvalidRepeatedShiftID() {
            int shiftID = 430;
            Time startTime = Time.valueOf("7:30:00");
            Time endTime = Time.valueOf("9:30:00");
            Date date = Date.valueOf("1993-04-20");

            Shift s1 = new Shift(shiftID,startTime, endTime, date, null);
            when(shiftRepository.findShiftByShiftId(shiftID)).thenReturn(s1);

            Shift s2 = new Shift(shiftID,startTime, endTime, date, null);

            HRSException e = assertThrows(HRSException.class, () -> shiftService.createShift(s2));
            assertEquals(e.getStatus(),HttpStatus.CONFLICT);
            assertEquals(e.getMessage(), "A shift with this ID already exists.");
      }

      @Test
      public void testCreateValidShiftWithEmployee() {
            int shiftID = 425;
            Time startTime = Time.valueOf("7:30:00");
            Time endTime = Time.valueOf("14:30:00");
            Date date = Date.valueOf("1993-04-20");
            String email = "janewhite@gmail.com";
            String name = "Jane White";
            int salary = 6000;
            Account account = new Account();
            Employee employee = new Employee(email,name,salary,account);

            Shift shift = new Shift(shiftID,startTime,endTime,date,employee);

            when(shiftRepository.save(shift)).thenReturn(shift);
            when(employeeRepository.findEmployeeByEmail(email)).thenReturn(employee);

            Shift outputShift = shiftService.createShift(shift);
            assertNotNull(outputShift);
            assertEquals(shift,outputShift);
            verify(shiftRepository,times(1)).save(shift);
      }
      /*
       * Tests if there is an existing shift with the same employee, date and start time
       */
      @Test
      public void testCreateShiftWithInvalidRepeatedDatesAndEmployee() {
            int shiftID = 430;
            int shiftID1 = 431;
            Time startTime = Time.valueOf("7:30:00");
            Time endTime = Time.valueOf("9:30:00");
            Date date = Date.valueOf("1993-04-20");
            String email = "janewhite@gmail.com";
            String name = "Jane White";
            int salary = 6000;
            Account account = new Account();

            Employee employee = new Employee(email,name,salary,account);
            Shift s1 = new Shift(shiftID,startTime,endTime,date,employee);
            when(shiftRepository.findShiftsByDateAndStartTime(date,startTime)).thenReturn(Collections.singletonList(s1));


            Shift s2 = new Shift(shiftID1,startTime, endTime, date, employee);

            HRSException e = assertThrows(HRSException.class, () -> shiftService.createShift(s2));
            assertEquals(e.getStatus(),HttpStatus.CONFLICT);
            assertEquals(e.getMessage(), "A shift with this start date, start time, and employee already exists.");
      }

      // write a test to determine overlapping shifts
      @Test
      public void testCreateShiftWithOverlappingTimes() {
            int shiftID1 = 430;
            int shiftID2 = 435;
            Time startTime1 = Time.valueOf("7:30:00");
            Time startTime2 = Time.valueOf("8:30:00");
            Time endTime1 = Time.valueOf("9:30:00");
            Time endTime2 = Time.valueOf("10:30:00");
            Date date = Date.valueOf("1993-04-20");
            String email = "janewhite@gmail.com";
            String name = "Jane White";
            int salary = 6000;
            Account account = new Account();

            Employee employee = new Employee(email,name,salary,account);

            Shift s1 = new Shift(shiftID1,startTime1,endTime1,date,employee);
            when(shiftRepository.findShiftsByDate(date)).thenReturn(Collections.singletonList(s1));

            Shift s2 = new Shift(shiftID2,startTime2,endTime2,date,employee);
            HRSException e = assertThrows(HRSException.class, () -> shiftService.createShift(s2));
            assertEquals(e.getStatus(),HttpStatus.CONFLICT);
            assertEquals(e.getMessage(), "The employee has an overlapping shift on this date.");
      }

      @Test
      public void testCreateShiftWithNonExistentEmployee() {
            int shiftID = 440;
            Time startTime = Time.valueOf("7:30:00");
            Time endTime = Time.valueOf("14:30:00");
            Date date = Date.valueOf("1993-04-20");

            String email = "janegill@gmail.com";
            String name = "Jane Gill";
            int salary = 6000;
            Account account = new Account();

            Employee employee = new Employee(email, name, salary, account);

            Shift shift = new Shift(shiftID, startTime, endTime, date, employee);

            // Mocking that employee doesn't exist in the system.
            when(employeeRepository.findEmployeeByEmail(email)).thenReturn(null);


            HRSException e = assertThrows(HRSException.class, () -> shiftService.createShift(shift));
            assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
            assertEquals(e.getMessage(), "Employee does not exist.");
      }

      @Test
      public void testDeleteNonExistentShift() {
            Shift shift = new Shift();
            int shiftID = 650;
            shift.setShiftId(shiftID);

            when(shiftRepository.findById(shiftID)).thenReturn(Optional.empty());

            HRSException e = assertThrows(HRSException.class, () -> shiftService.deleteShift(shift));
            assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
            assertEquals(e.getMessage(), "Shift does not exist.");
      }

}
