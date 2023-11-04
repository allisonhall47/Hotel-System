package ca.mcgill.ecse321.hotelsystem.dto;

import ca.mcgill.ecse321.hotelsystem.Model.Account;

import java.sql.Date;
import java.time.LocalDate;

public class AccountRequestDto {

    private String password;
    private String address;
    private LocalDate dob;

    public AccountRequestDto() {}

    public AccountRequestDto(String password, String address, LocalDate dob){
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

    public LocalDate getdob() {
        return dob;
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
