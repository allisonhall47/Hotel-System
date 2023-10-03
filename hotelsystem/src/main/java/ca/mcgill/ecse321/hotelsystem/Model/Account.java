package ca.mcgill.ecse321.hotelsystem.Model;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int accountNumber;
    private String password;
    private String address;
    private Date dob;


    public Account(String password, String address, Date dob) {
        this.accountNumber = accountNumber;
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

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
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
