package ca.mcgill.ecse321.hotelsystem.controller;

import ca.mcgill.ecse321.hotelsystem.Model.Account;
import ca.mcgill.ecse321.hotelsystem.dto.AccountRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.AccountRequestUpdateDto;
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

    @PostMapping(value = { "/account/create", "/account/create/"})
    public ResponseEntity<AccountResponseDto> createAccount(@RequestBody AccountRequestDto accountRequest){
        Account account = accountRequest.toModel();
        account = accountService.createAccount(account);
        return new ResponseEntity<AccountResponseDto>(new AccountResponseDto(account), HttpStatus.CREATED);
    }

    @PutMapping(value = { "/account/update", "/account/update/"})
    public ResponseEntity<AccountResponseDto> updateAccount(@RequestBody AccountRequestUpdateDto accountRequest){
        Account account = accountRequest.toModel();
        account = accountService.updateAccount(account);
        return new ResponseEntity<AccountResponseDto>(new AccountResponseDto(account), HttpStatus.OK);
    }

    @GetMapping(value = {"/account", "/account/"})
    public ResponseEntity<AccountResponseDto> getAccountByEmail(@RequestParam int accountNumber){
        return new ResponseEntity<AccountResponseDto>(new AccountResponseDto(accountService.getAccountByAccountNumber(accountNumber)), HttpStatus.OK);
    }

    @DeleteMapping(value = {"/account/delete/{num}"})
    public void deleteAccount(@PathVariable int accountNumber){
        accountService.deleteAccount(accountNumber);
    }


}
