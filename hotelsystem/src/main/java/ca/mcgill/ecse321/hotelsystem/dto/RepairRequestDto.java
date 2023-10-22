package ca.mcgill.ecse321.hotelsystem.dto;

import ca.mcgill.ecse321.hotelsystem.Model.Employee;
import ca.mcgill.ecse321.hotelsystem.Model.Repair;
import ca.mcgill.ecse321.hotelsystem.Model.CompletionStatus;

public class RepairRequestDto {

    private int repairId;
    private String description;
    private String employeeEmail;
    private CompletionStatus status;

    public RepairRequestDto(int repairId, String description, String employeeEmail, CompletionStatus status){
        this.repairId = repairId;
        this.description = description;
        this.employeeEmail = employeeEmail;
        this.status = status;
    }

    public Repair toModel(Employee e){
        return new Repair(status, description, e);
    }

    public int getRepairId() {
        return repairId;
    }

    public void setRepairId(int repairId) {
        this.repairId = repairId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public CompletionStatus getStatus() {
        return status;
    }

    public void setStatus(CompletionStatus status) {
        this.status = status;
    }
}
