package ca.mcgill.ecse321.hotelsystem.dto;

public class RepairRequestDto {

    private String description;
    private int employeeId;
    //TODO: employee id or email??
    public RepairRequestDto(String description, int employeeId){
        this.description = description;
        this.employeeId = employeeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int id) {
        this.employeeId = id;
    }

}
