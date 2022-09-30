package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LoanController {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientLoanRepository clientLoanRepository;
    @Autowired
    ClientService clientService;
    @Autowired
    AccountService accountService;
    @Autowired
    LoanService loanService;
    @Autowired
    ClientLoanService clientLoanService;
    @Autowired
    TransactionService transactionService;

    @Transactional
    @PostMapping("/api/loans")
    public ResponseEntity <Object> createLoan(
            @RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication
    ){
        Client client = clientService.findClientByEmail(authentication.getName());
        Account account = accountService.getClientByNumber(loanApplicationDTO.getAccountNumberTo());
        Loan loan = loanService.findByName(loanApplicationDTO.getloanName());
        if (loanApplicationDTO.getAccountNumberTo().isEmpty() || loanApplicationDTO.getAmount() <= 0 || loanApplicationDTO.getPayments() <=0){
            return new ResponseEntity <>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (loan == null){
            return new ResponseEntity<>("The loan does not exist", HttpStatus.FORBIDDEN);
        }
        if (loan.getMaxAmount() < loanApplicationDTO.getAmount()){
            return new ResponseEntity<>("The requested amount is exceeded",HttpStatus.FORBIDDEN);
        }
        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())){
            return new ResponseEntity<>("You cannot apply this amount of installments to the next loan",HttpStatus.FORBIDDEN);
        }
        if (account == null){
            return new ResponseEntity<>("Destination account does not exist",HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(account)){
            return new ResponseEntity<>("That account does not belong to you",HttpStatus.FORBIDDEN);
        }
        if(client.getClientLoans().stream().filter(clientLoan -> clientLoan.getLoan()==loan).count()>0){
            return new ResponseEntity<>("You already have an active"+ " " +loan.getName(),HttpStatus.FORBIDDEN);
        }
        if (!account.isAccountActive()){
            return new ResponseEntity<>("This account is disabled to request a loan",HttpStatus.FORBIDDEN);
        }
        else {
            ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount(), loanApplicationDTO.getPayments(), client, loan);
            if (loan.getName().equals("Personal")) {
                clientLoan.setAmount(clientLoan.getAmount() * 1.20);
                if (clientLoan.getPayments() == 12) {
                    clientLoan.setAmount(clientLoan.getAmount() * 1.25);
                } else if (clientLoan.getPayments() == 24) {
                    clientLoan.setAmount(clientLoan.getAmount() * 1.30);
                }
            }
            if (loan.getName().equals("Mortgage")) {
                clientLoan.setAmount(clientLoan.getAmount() * 1.20);
                if (clientLoan.getPayments() == 12) {
                    clientLoan.setAmount(clientLoan.getAmount() * 1.25);
                } else if (clientLoan.getPayments() == 24) {
                    clientLoan.setAmount(clientLoan.getAmount() * 1.30);
                } else if (clientLoan.getPayments() == 36) {
                    clientLoan.setAmount(clientLoan.getAmount() * 1.40);
                } else if (clientLoan.getPayments() == 48) {
                    clientLoan.setAmount(clientLoan.getAmount() * 1.45);
                } else if (clientLoan.getPayments() == 60) {
                    clientLoan.setAmount(clientLoan.getAmount() * 1.50);
                }
            }
            if (loan.getName().equals("Car")) {
                clientLoan.setAmount(clientLoan.getAmount() * 1.20);
                if (clientLoan.getPayments() == 6) {
                    clientLoan.setAmount(clientLoan.getAmount() * 1.25);
                } else if (clientLoan.getPayments() == 12) {
                    clientLoan.setAmount(clientLoan.getAmount() * 1.30);
                } else if (clientLoan.getPayments() == 24) {
                    clientLoan.setAmount(clientLoan.getAmount() * 1.35);
                } else if (clientLoan.getPayments() == 36) {
                    clientLoan.setAmount(clientLoan.getAmount() * 1.40);
                }
            }
            clientLoanService.saveClientLoan(clientLoan);
            Transaction transaction = new Transaction(account, TransactionType.CREDIT,clientLoan.getAmount(),"Successfully requested loan", LocalDateTime.now());
            transactionService.saveTransactions(transaction);
            account.setBalance(account.getBalance() + loanApplicationDTO.getAmount());
            accountService.saveAccount(account);
            return new ResponseEntity<>("Successfully requested loan", HttpStatus.CREATED);
        }
    }
    @RequestMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanService.getAllLoans().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList());
    }
}