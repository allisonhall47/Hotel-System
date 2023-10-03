package ca.mcgill.ecse321.hotelsystem.Model;

import jakarta.persistence.*;

@Entity
public class Owner extends User {



    public Owner(String email, String name, Account account) {
        super(email, name, account);
    }

    public Owner() {
    }
}
