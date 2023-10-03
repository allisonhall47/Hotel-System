package ca.mcgill.ecse321.hotelsystem.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Customer extends User{

    protected Customer() {

    }

    public Customer(String email, String name, Account account) {
        super(email, name, account);
    }
}
