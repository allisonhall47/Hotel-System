package ca.mcgill.ecse321.hotelsystem.Model;

import jakarta.persistence.Entity;

@Entity
public class Employee extends User {

    private int salary;

    protected Employee() {

    }

    public Employee(String email, String name, Account account, int salary) {
        super(email, name, account);
        this.salary = salary;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

}
