package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    AccountService accountService;
    @Autowired
    ClientService clientService;
    @Autowired
    TransactionService transactionService;

    @RequestMapping("/accounts")
    public List <AccountDTO> getAccounts(){
        return accountService.getAllAccounts().stream().map(AccountDTO::new).collect(Collectors.toList());
    }
    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return new AccountDTO(accountService.getAccountById(id));
    }
    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(@RequestParam AccountType accountType, Authentication authentication){
        Client client = clientService.findClientByEmail(authentication.getName());
        List<Account> accountList = client.getAccounts().stream().filter(activeAccount -> activeAccount.isAccountActive()).collect(Collectors.toList());
        if ( accountList.toArray().length>=3) {
            return new ResponseEntity<>("You already have more than 3 accounts created...", HttpStatus.FORBIDDEN);
        }else {
            Random randomNumber = new Random();
            Account account = new Account("VIN-"+randomNumber.nextInt(100000000), LocalDate.now(), 00.00, client, accountType);
            accountService.saveAccount(account);
            account.setBalance(750.00);
            return new ResponseEntity<>("Your account was successfully created...",HttpStatus.CREATED);
        }
    }
    @PatchMapping("/clients/current/accounts")
    public ResponseEntity <Object> deleteAccount(
            @RequestParam String number, Authentication authentication
    ){
        Client client = clientService.findClientByEmail(authentication.getName());
        Account account = accountService.getClientByNumber(number);
        if (number.isEmpty()){
            return new ResponseEntity<>("Please select account",HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(account)){
            return new ResponseEntity<>("This account is not yours",HttpStatus.FORBIDDEN);
        }
        if (account == null){
            return new ResponseEntity<>("This account doesn't exist",HttpStatus.FORBIDDEN);
        }
        account.setAccountActive(false);
        accountService.saveAccount(account);
        return new ResponseEntity<>("Account deleted succesfully", HttpStatus.ACCEPTED);
    }
}