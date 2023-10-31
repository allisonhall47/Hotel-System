package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.Employee;
import ca.mcgill.ecse321.hotelsystem.Model.Shift;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.repository.CustomerRepository;
import ca.mcgill.ecse321.hotelsystem.repository.EmployeeRepository;
import ca.mcgill.ecse321.hotelsystem.repository.ShiftRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.sql.Date;
import java.sql.Time;


@Service
public class ShiftService {

    @Autowired
    ShiftRepository shiftRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    /**
     * GetAllShifts: service method to fetch all existing shifts in the database
     * @return List of shifts
     * @throws HRSException if no shifts exist in the system
     */
    @Transactional
    public List<Shift> getAllShifts(){
        List<Shift> shifts = shiftRepository.findAll();
        if (shifts.size() == 0){
            throw new HRSException(HttpStatus.NOT_FOUND, "There are no shifts in the system.");
        }
        return shifts;
    }
    /**
     * getShiftByShiftID: service ID to get the shift corresponding to the shift ID
     * @param shiftID: shift ID of the employee
     * @return shift
     * @throws HRSException if the shift doesn't exist
     */
    @Transactional
    public Shift getShiftByShiftID(int shiftID) {
        if (shiftID < 0) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "Invalid shift ID.");
        }
        Shift shift = shiftRepository.findShiftByShiftId(shiftID);
        if (shift == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, "Shift not found.");
        }
        return shift;
    }
    /**
     * getShiftsByEmployeeEmail: service email to fetch an existing shift list corresponding to an employee's email
     * @param email: email of the employee
     * @return list of shifts
     * @throws HRSException if the list doesn't exist
     */
    @Transactional
    public List<Shift> getShiftsByEmployeeEmail(String email) {
        List<Shift> shiftList = shiftRepository.findShiftsByEmployeeEmail(email);
        if (shiftList == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, "Shift list for this email not found.");
        }
        return shiftList;
    }
    /**
     * getShiftsByEmployeeEmail: service email to fetch an existing shift list corresponding to an certain date
     * @param date: date of the shift
     * @return list of shifts
     * @throws HRSException if the list doesn't exist
     */
    @Transactional
    public List<Shift> getShiftsByDate(Date date) {
        List<Shift> sdList = shiftRepository.findShiftsByDate(date);
        if (sdList == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, "Shift list for this date not found. ");
        }
        return sdList;
    }

    @Transactional
    public List<Shift> getShiftsByDateAndStartTime(Date date, Time startTime) {
        List<Shift> stList = shiftRepository.findShiftsByDateAndStartTime(date, startTime);
        if (stList == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, "Shift list for this date and time does not exist.");
        }
        return stList;
    }
    @Transactional
    public Shift createShift(Shift shift) {
        isValidShift(shift);
        if ((shiftRepository.findShiftByShiftId(shift.getShiftId()) == null)) {
            return shiftRepository.save(shift);
        }
        throw new HRSException(HttpStatus.CONFLICT, "A shift with this ID already exists.");
    }

    @Transactional
    public void deleteShift(Shift shift) {
        if (!shiftRepository.existsById(shift.getShiftId())) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "Shift does not exist.");
        }
        shiftRepository.delete(shift);
    }

    @Transactional
    public Shift updateShift(Shift shift) {
        isValidShift(shift);
        Shift previousShift = getShiftByShiftID(shift.getShiftId());
        if (previousShift == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, "Shift not found.");
        }
        // setters and getters to test
        previousShift.setShiftId(shift.getShiftId());
        previousShift.setDate(shift.getDate());
        previousShift.setStartTime(shift.getStartTime());
        previousShift.setShiftId(shift.getShiftId());
        previousShift.setEmployee(shift.getEmployee());

        return shiftRepository.save(previousShift);
    }

    private void isValidShift(Shift shift) {
        if (shift == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, "Shift not found.");
        }
        if (shift.getStartTime() == null || shift.getEndTime() == null || shift.getDate() == null) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "Empty fields are present.");
        }
        if (shift.getStartTime().after(shift.getEndTime())) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "Invalid start/end times.");
        }
        // checks if an employee already has a shift at the same date and time
        List<Shift> shiftsWithSameDateAndTime = shiftRepository.findShiftsByDateAndStartTime(shift.getDate(), shift.getStartTime());

        if (shiftsWithSameDateAndTime.stream().anyMatch(s -> s.getEmployee().getEmail().equals(shift.getEmployee().getEmail()))) {
            throw new HRSException(HttpStatus.CONFLICT, "A shift with this start date, start time, and employee already exists.");
        }

        // write to determine no person overlaps shift
        // same employee, same date, no overlap can occur
        List<Shift> shiftsOnSameDate = shiftRepository.findShiftsByDate(shift.getDate());

        if (shiftsOnSameDate.stream().anyMatch(existingShift ->
                existingShift.getEmployee().getEmail().equals(shift.getEmployee().getEmail()) &&
                        (shift.getStartTime().before(existingShift.getEndTime()) &&
                                shift.getEndTime().after(existingShift.getStartTime())))) {

            throw new HRSException(HttpStatus.CONFLICT, "The employee has an overlapping shift on this date.");
        }

        Employee employee = shift.getEmployee();

        if (shift.getEmployee() != null && employeeRepository.findEmployeeByEmail(employee.getEmail()) == null) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "Employee does not exist.");
        }
    }
}
