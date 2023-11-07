package ca.mcgill.ecse321.hotelsystem.dto;

import ca.mcgill.ecse321.hotelsystem.Model.Account;
import ca.mcgill.ecse321.hotelsystem.Model.Employee;

public class EmployeeRequestDto {
    private String name;
    private String email;
    private int salary;

    private int accountNumber;

    public EmployeeRequestDto(){}
    public EmployeeRequestDto(String name, String email, int salary, int accountNumber){
        this.name = name;
        this.email = email;
        this.salary = salary;
        this.accountNumber = accountNumber;
    }

    public Employee toModel(Account account) {
        return new Employee(email, name, salary, account);
    }

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
