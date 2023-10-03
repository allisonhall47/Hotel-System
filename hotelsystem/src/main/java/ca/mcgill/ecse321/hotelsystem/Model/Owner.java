package ca.mcgill.ecse321.hotelsystem.Model;

import jakarta.persistence.Entity;

@Entity
public class Owner extends User {

    protected Owner() {

    }

    public Owner(String email, String name, Account account) {
        super(email, name, account);
    }
}
