package ca.mcgill.ecse321.hotelsystem.dto;

import ca.mcgill.ecse321.hotelsystem.Model.Account;
import ca.mcgill.ecse321.hotelsystem.Model.Customer;

public class CustomerResponseDto {
    private String name;
    private String email;

    private int accountNumber;

    public CustomerResponseDto(Customer customer){
        this.name = customer.getName();
        this.email = customer.getEmail();
        if (customer.getAccount() != null) this.accountNumber = customer.getAccount().getAccountNumber();
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
