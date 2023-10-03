package ca.mcgill.ecse321.hotelsystem.Model;

import java.sql.Date;
import java.sql.Time;

@Entity
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int shiftId;
    private Time startTime;
    private Time endTime;
    private Date date;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "email")
    private Employee employee;

    public Shift(Time startTime, Time endTime, Date date, Employee employee) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.employee = employee;
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

    public Date getDate() {
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

    public void setDate(Date date) {
        this.date = date;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
