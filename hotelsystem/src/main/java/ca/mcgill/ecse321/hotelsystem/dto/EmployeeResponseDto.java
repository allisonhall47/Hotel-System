package ca.mcgill.ecse321.hotelsystem.dto;

import ca.mcgill.ecse321.hotelsystem.Model.Employee;

public class EmployeeResponseDto {
    private String name;
    private String email;
    private int salary;

    private int accountNumber;

    public EmployeeResponseDto(Employee employee){
        this.name = employee.getName();
        this.email = employee.getEmail();
        this.salary = employee.getSalary();
        if (employee.getAccount() != null) this.accountNumber = employee.getAccount().getAccountNumber();

    }

    public EmployeeResponseDto() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

}
