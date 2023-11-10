package ca.mcgill.ecse321.hotelsystem.dto;

import ca.mcgill.ecse321.hotelsystem.Model.Owner;

public class OwnerResponseDto {
    private String name;
    private String email;

    private int accountNumber;


    public OwnerResponseDto(Owner owner){
        this.name = owner.getName();
        this.email = owner.getEmail();
        if (owner.getAccount() != null) this.accountNumber = owner.getAccount().getAccountNumber();

    }

    public OwnerResponseDto() {}

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
