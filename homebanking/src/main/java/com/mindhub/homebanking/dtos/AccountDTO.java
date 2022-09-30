package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {
        private long id;
        private String number;
        private LocalDate creationDate;
        private double balance;
        private long clientId;
        private Set<TransactionDTO> transactions;
        private boolean accountActive;
        private AccountType accountType;


        public  AccountDTO(){}
        public AccountDTO(Account account){
            this.id = account.getId();
            this.number = account.getNumber();
            this.creationDate = account.getCreationDate();
            this.balance = account.getBalance();
            this.clientId = account.getClient().getId();
            this.transactions = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toSet());
            this.accountActive = account.isAccountActive();
            this.accountType = account.getAccountType();
        }

        public long getId() {return id;}

        public String getNumber() {return number;}

        public LocalDate getCreationDate() {return creationDate;}

        public double getBalance() {return balance;}

        public Set<TransactionDTO> getTransactions() {
        return transactions;
    }

        public long getClientId() {return clientId;}

        public boolean isAccountActive() {return accountActive;}

        public AccountType getAccountType() {return accountType;}
}
