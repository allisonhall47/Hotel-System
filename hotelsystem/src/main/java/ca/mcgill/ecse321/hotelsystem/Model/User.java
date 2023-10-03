package ca.mcgill.ecse321.hotelsystem.Model;


import jakarta.persistence.*;

@MappedSuperclass
public abstract class User {
    @Id
    private String email;
    private String name;

    //TODO in the model diagram , there is uni directional association from Account to User, should we remove this field ?
    @OneToOne
    @JoinColumn(name = "accountNumber")
    private Account account;

    protected User() {
    }

    public User(String email, String name, Account account) {
        this.email = email;
        this.name = name;
        this.account = account;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
