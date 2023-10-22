package ca.mcgill.ecse321.hotelsystem.dto;

import ca.mcgill.ecse321.hotelsystem.Model.Account;
import ca.mcgill.ecse321.hotelsystem.Model.Customer;

public class CustomerRequestDto {

    private String name;
    private String email;

    private int accountNumber;

    public CustomerRequestDto(String name, String email, int accountNumber){
        this.name = name;
        this.email = email;
        this.accountNumber = accountNumber;
    }

    public Customer toModel(Account account){
        return new Customer(email, name, account);
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
