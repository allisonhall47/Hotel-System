package ca.mcgill.ecse321.hotelsystem.dto;

import ca.mcgill.ecse321.hotelsystem.Model.CompletionStatus;
import ca.mcgill.ecse321.hotelsystem.Model.Employee;
import ca.mcgill.ecse321.hotelsystem.Model.Repair;

public class RepairResponseDto {
    private int repairId;
    private String description;
    private String employeeEmail;
    private CompletionStatus status;

    public RepairResponseDto(Repair repair){
        this.repairId = repair.getRepairId();
        this.description = repair.getDescription();
        if (repair.getEmployee() != null) this.employeeEmail = repair.getEmployee().getEmail();
        this.status = repair.getStatus();
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
