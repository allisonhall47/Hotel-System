package ca.mcgill.ecse321.hotelsystem.Model;

import jakarta.persistence.Entity;

@Entity
public class Customer extends User{


    public Customer(String email, String name, Account account) {
        super(email, name, account);
    }

    public Customer() {
    }

}
