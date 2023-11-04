package ca.mcgill.ecse321.hotelsystem.Model;

import jakarta.persistence.*;

import java.sql.Date;
import java.time.LocalDate;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int accountNumber;
    private String password;
    private String address;
    private LocalDate dob;

    public Account(String password, String address, LocalDate dob) {
        this.password = password;
        this.address = address;
        this.dob = dob;
    }

    public Account() {
    }


    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }


    @Override
    public String toString() {
        return "Account{" +
                "accountNumber=" + accountNumber +
                ", password='" + password + '\'' +
                ", address='" + address + '\'' +
                ", dob=" + dob +
                '}';
    }
}
