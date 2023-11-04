package ca.mcgill.ecse321.hotelsystem.dto;

import ca.mcgill.ecse321.hotelsystem.Model.Account;

import java.sql.Date;
import java.time.LocalDate;

public class AccountResponseDto {

    private String password;
    private String address;
    private LocalDate dob;
    private int accountNumber;

    public AccountResponseDto(){}

    public AccountResponseDto(Account account){
        this.password = account.getPassword();
        this.address = account.getAddress();
        this.dob = account.getDob();
        this.accountNumber = account.getAccountNumber();
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getdob() {
        return dob;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setdob(LocalDate dob) {
        this.dob = dob;
    }
}
