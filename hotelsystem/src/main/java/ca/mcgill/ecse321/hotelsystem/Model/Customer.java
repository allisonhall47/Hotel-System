package ca.mcgill.ecse321.hotelsystem.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Customer extends User{



    public Customer(String email, String name) {
        super(email, name);
    }

    public Customer() {
    }
}
