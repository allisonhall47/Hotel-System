package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.Account;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

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
        if(accounts.isEmpty()){
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
     * @param account: account to create
     * @return created account
     */
    @Transactional
    public Account createAccount(Account account){
        isValidAccount(account);
        account = accountRepository.save(account);
        return account;
    }

    /**
     * UpdateAccount: service method to update information in an account
     * @param account: account with new information
     * @param accountNumber: number for account to update
     * @return updated account
     * @throws HRSException if the account is not found
     */
    @Transactional
    public Account updateAccount(Account account, int accountNumber){
        isValidAccount(account);

        Account oldAccount = getAccountByAccountNumber(accountNumber);
        if (oldAccount == null){
            throw new HRSException(HttpStatus.NOT_FOUND, "Account not found.");
        }

        oldAccount.setDob(account.getDob());
        oldAccount.setPassword(account.getPassword());
        oldAccount.setAddress(account.getAddress());
        return accountRepository.save(oldAccount);
    }

    /**
     * DeleteAccount: service method to delete an account
     * @param accountNumber: account number for the account to delete
     * @throws HRSException if the account is not found
     */
    @Transactional
    public void deleteAccount(int accountNumber){
        Account account = accountRepository.findAccountByAccountNumber(accountNumber);
        if(account == null){
            throw new HRSException(HttpStatus.NOT_FOUND, "Account not found.");
        }
        accountRepository.deleteAccountByAccountNumber(accountNumber);
    }

    /**
     * IsValidAccount: checks if account is valid to update or create
     * @param account: account to check if valid
     * @throws HRSException if account has an empty field or an invalid password
     */
    private void isValidAccount(Account account) {
        if(account.getAddress() == null || account.getDob() == null || account.getPassword() == null){
            throw new HRSException(HttpStatus.BAD_REQUEST, "Empty field in the account");
        }
        if(!Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{6,}$").matcher(account.getPassword()).find()){
            throw new HRSException(HttpStatus.BAD_REQUEST, "Invalid Password");
        }
        if(account.getDob().isAfter(LocalDate.now())){
            throw new HRSException(HttpStatus.BAD_REQUEST, "Invalid date of birth.");

        }

//        if(account.getDob().after(new Date(System.currentTimeMillis()))){
//            throw new HRSException(HttpStatus.BAD_REQUEST, "Invalid date of birth.");
//        }
    }

}
