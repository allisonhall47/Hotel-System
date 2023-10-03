package ca.mcgill.ecse321.hotelsystem.Model;

import jakarta.persistence.Entity;

@Entity
public class Employee extends User {

    private int salary;

    public Employee(String email, String name, int salary) {
        super(email, name);
        this.salary = salary;
    }

    public Employee() {
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

}
