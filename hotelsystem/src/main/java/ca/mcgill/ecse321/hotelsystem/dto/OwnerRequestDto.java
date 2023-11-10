package ca.mcgill.ecse321.hotelsystem.dto;

import ca.mcgill.ecse321.hotelsystem.Model.Account;
import ca.mcgill.ecse321.hotelsystem.Model.Owner;

public class OwnerRequestDto {

    private String name;
    private String email;

    private int accountNumber;

    public OwnerRequestDto(){};

    public OwnerRequestDto(String name, String email, int accountNumber){
        this.name = name;
        this.email = email;
        this.accountNumber = accountNumber;
    }

    public Owner toModel(Account account){
        return new Owner(email, name, account);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

}
