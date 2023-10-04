package ca.mcgill.ecse321.hotelsystem.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Customer extends User{

    private int points;

    public Customer(String email, String name, Account account, int points) {
        super(email, name, account);
        this.points = points;
    }

    public Customer() {
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
