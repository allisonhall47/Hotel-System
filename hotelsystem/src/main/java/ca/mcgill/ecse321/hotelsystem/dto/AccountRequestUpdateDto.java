package ca.mcgill.ecse321.hotelsystem.dto;

import ca.mcgill.ecse321.hotelsystem.Model.Account;

import java.sql.Date;

public class AccountRequestUpdateDto {
    private String password;
    private String address;
    private Date dob;
    private int accountNumber;

    public AccountRequestUpdateDto(String password, String address, Date dob, int accountNumber){
        this.password = password;
        this.address = address;
        this.dob = dob;
        this.accountNumber = accountNumber;
    }

    public Account toModel(){
        Account a = new Account(password, address, dob);
        a.setAccountNumber(accountNumber);
        return a;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public Date getdob() {
        return dob;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setdob(Date dob) {
        this.dob = dob;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }
}
