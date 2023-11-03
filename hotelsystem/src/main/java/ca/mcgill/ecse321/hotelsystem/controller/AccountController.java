package ca.mcgill.ecse321.hotelsystem.controller;

import ca.mcgill.ecse321.hotelsystem.Model.Account;
import ca.mcgill.ecse321.hotelsystem.dto.AccountRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.AccountResponseDto;
import ca.mcgill.ecse321.hotelsystem.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins="*")
@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    /**
     * GetAllAccounts: get a list of all accounts in the system
     * @return list of account response dtos
     */
    @GetMapping(value = {"/accounts", "/accounts/"})
    public List<AccountResponseDto> getAllAccounts(){
        return accountService.getAllAccounts().stream().map(AccountResponseDto::new).collect(Collectors.toList());
    }

    /**
     * CreateAccount: create an account
     * @param accountRequest: information for the new account
     * @return response entity containing the response dto object
     */
    @PostMapping(value = { "/account/create", "/account/create/"})
    public ResponseEntity<AccountResponseDto> createAccount(@RequestBody AccountRequestDto accountRequest){
        Account account = accountRequest.toModel();
        account = accountService.createAccount(account);
        return new ResponseEntity<AccountResponseDto>(new AccountResponseDto(account), HttpStatus.CREATED);
    }

    /**
     * UpdateAccount: update the information in a pre-existing account
     * @param id: accountNumber for account to update
     * @param accountRequest: information to update the account with
     * @return response entity containing the response dto object
     */
    @PutMapping(value = { "/account/{id}", "/account/{id}/"})
    public ResponseEntity<AccountResponseDto> updateAccount(@PathVariable int id, @RequestBody AccountRequestDto accountRequest){
        Account account = accountRequest.toModel();
        account = accountService.updateAccount(account, id);
        return new ResponseEntity<AccountResponseDto>(new AccountResponseDto(account), HttpStatus.OK);
    }

    /**
     * GetAccountByNumber: retrieve an account in the system by account number
     * @param accountNumber: number of account to retrieve
     * @return response entity containing the retrieved account
     */
    @GetMapping(value = {"/account", "/account/"})
    public ResponseEntity<AccountResponseDto> getAccountByNumber(@RequestParam int accountNumber){
        return new ResponseEntity<AccountResponseDto>(new AccountResponseDto(accountService.getAccountByAccountNumber(accountNumber)), HttpStatus.OK);
    }

    /**
     * DeleteAccount: delete an account from the system
     * @param accountNumber: number of the account to delete
     */
    @DeleteMapping(value = {"/account/delete/{accountNumber}"})
    public void deleteAccount(@PathVariable int accountNumber){
        accountService.deleteAccount(accountNumber);
    }


}
