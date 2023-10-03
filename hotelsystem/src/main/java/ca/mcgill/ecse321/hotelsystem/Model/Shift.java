package ca.mcgill.ecse321.hotelsystem.Model;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.sql.Date;
import java.sql.Time;

public class Shift {

    private Time startTime;
    private Time endTime;
    private Date date;

    @ManyToOne
    private Employee employee;
}
