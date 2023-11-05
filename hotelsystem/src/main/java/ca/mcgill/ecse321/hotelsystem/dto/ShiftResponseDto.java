package ca.mcgill.ecse321.hotelsystem.dto;

import ca.mcgill.ecse321.hotelsystem.Model.Employee;
import ca.mcgill.ecse321.hotelsystem.Model.Shift;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

public class ShiftResponseDto {
    private Time startTime;
    private Time endTime;
    private LocalDate date;

    private String employeeEmail;

    private int shiftId;

    public ShiftResponseDto() {
        // NO FIELDS CONSTRUCTOR
    }

    public ShiftResponseDto(Shift shift){
        this.startTime = shift.getStartTime();
        this.endTime = shift.getEndTime();
        this.date = shift.getDate();
        this.shiftId = shift.getShiftId();
        if (shift.getEmployee() != null) this.employeeEmail = shift.getEmployee().getEmail();
    }

    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public LocalDate getDate() {
        return date;
    }


    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public String toString() {
        return "ShiftResponseDto{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", date=" + date +
                ", employeeEmail='" + employeeEmail + '\'' +
                '}';
    }

}
