package ca.mcgill.ecse321.hotelsystem.Model;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int shiftId;
    private Time startTime;
    private Time endTime;
    private LocalDate date;
    
    @ManyToOne
    private Employee employee;

    public Shift(Time startTime, Time endTime, LocalDate date, Employee employee) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.employee = employee;
    }

    public Shift() {
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    public int getShiftId() {
        return shiftId;
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

    public Employee getEmployee() {
        return employee;
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

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
