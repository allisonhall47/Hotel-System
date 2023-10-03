package ca.mcgill.ecse321.hotelsystem.Model;

import jakarta.persistence.*;

@Entity
public class Repair {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int repairId;
    
    private CompletionStatus status;
    private String description;

    @ManyToOne
    private Employee employee;

    @SuppressWarnings("unused")
    public Repair() {
    }

    public Repair(CompletionStatus status, String description, Employee employee) {
        this.status = status;
        this.description = description;
        this.employee = employee;
    }

    public int getRepairId() {
        return repairId;
    }

    public CompletionStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setStatus(CompletionStatus status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Repair{" +
                "status=" + status +
                ", description='" + description + '\'' +
                ", employee=" + employee +
                '}';
    }
}
