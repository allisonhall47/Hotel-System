package ca.mcgill.ecse321.hotelsystem.dto;

import ca.mcgill.ecse321.hotelsystem.Model.Employee;
import ca.mcgill.ecse321.hotelsystem.Model.Shift;

import java.sql.Date;
import java.sql.Time;

public class ShiftResponseDto {
    private Time startTime;
    private Time endTime;
    private Date date;
    private int shiftId;

    private String employeeEmail;

    public ShiftResponseDto(Shift shift){
        this.startTime = shift.getStartTime();
        this.endTime = shift.getEndTime();
        this.date = shift.getDate();
        this.shiftId = shift.getShiftId();
        if (shift.getEmployee() != null) this.employeeEmail = shift.getEmployee().getEmail();
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public Date getDate() {
        return date;
    }

    public int getShiftId() {
        return shiftId;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

}
