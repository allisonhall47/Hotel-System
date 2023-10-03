package ca.mcgill.ecse321.hotelsystem.Model;

import jakarta.persistence.ManyToOne;

public class Repair {
    private CompletionStatus status;
    private String description;

    @ManyToOne
    private Employee employee;
}
