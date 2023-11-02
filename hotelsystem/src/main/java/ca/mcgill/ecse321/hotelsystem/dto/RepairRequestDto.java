package ca.mcgill.ecse321.hotelsystem.dto;

public class RepairRequestDto {

    private String description;
    private String employeeEmail;

    public RepairRequestDto(String description, String employeeEmail){
        this.description = description;
        this.employeeEmail = employeeEmail;
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

}
