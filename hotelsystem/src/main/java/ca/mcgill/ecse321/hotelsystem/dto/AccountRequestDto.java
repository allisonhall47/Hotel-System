package ca.mcgill.ecse321.hotelsystem.dto;

import ca.mcgill.ecse321.hotelsystem.Model.Account;

import java.sql.Date;

public class AccountRequestDto {

    private String password;
    private String address;
    private Date dob;

    public AccountRequestDto(String password, String address, Date dob){
        this.password = password;
        this.address = address;
        this.dob = dob;
    }

    public Account toModel(){
        return new Account(password, address, dob);
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
}
