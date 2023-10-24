package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.Account;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    /**
     * GetAllAccounts: service method to fetch all existing accounts in the database
     * @return List of accounts
     * @throws HRSException if no accounts exist in the system
     */
    @Transactional
    public List<Account> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        if(accounts.size() == 0){
            throw new HRSException(HttpStatus.NOT_FOUND, "There are no accounts in the system.");
        }
        return accounts;
    }

    /**
     * GetAccountByAccountNumber: service number to fetch an existing account with a specific account number
     * @param accountNumber: number of the account
     * @return account
     * @throws HRSException if the account does not exist
     */
    @Transactional
    public Account getAccountByAccountNumber(int accountNumber) {
        Account account = accountRepository.findAccountByAccountNumber(accountNumber);
        if (account == null){
            throw new HRSException(HttpStatus.NOT_FOUND, "Account not found.");
        }
        return account;
    }

    /**
     * CreateAccount: service method to create and store an account in the database
     * @param password: password for the account
     * @param address: address for the account
     * @param dob: date of birth for the account
     * @return created account
     */
    @Transactional
    public Account createAccount(String password, String address, Date dob){
        Account account = new Account(password, address, dob);
        accountRepository.save(account);
        return account;
    }

    /**
     * UpdateAccount: service method to update information in an account
     * @param account: account with new information
     * @return updated account
     */
    @Transactional
    public Account updateAccount(Account account){
        Account oldAccount = getAccountByAccountNumber(account.getAccountNumber());
        oldAccount.setDob(account.getDob());
        oldAccount.setPassword(account.getPassword());
        oldAccount.setAddress(account.getAddress());
        return accountRepository.save(oldAccount);
    }

}
