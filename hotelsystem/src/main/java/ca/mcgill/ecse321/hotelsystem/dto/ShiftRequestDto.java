package ca.mcgill.ecse321.hotelsystem.dto;

import ca.mcgill.ecse321.hotelsystem.Model.Employee;
import ca.mcgill.ecse321.hotelsystem.Model.Shift;

import java.sql.Date;
import java.sql.Time;

public class ShiftRequestDto {

    private Time startTime;
    private Time endTime;
    private Date date;

    private String employeeEmail;

    public ShiftRequestDto(Time startTime, Time endTime, Date date) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;;
    }

    public Shift toModel(Employee employee){
        return new Shift(startTime, endTime, date, employee);
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
