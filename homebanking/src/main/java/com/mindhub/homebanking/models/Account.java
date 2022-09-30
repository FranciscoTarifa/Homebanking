package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String number;
    private LocalDate creationDate;
    private double balance;
    private boolean accountActive;
    private AccountType accountType;

    public Account(){}

    public Account(String number, LocalDate creationDate, double balance,AccountType accountType){
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.accountActive = true;
        this.accountType= accountType;
    }

    public Account(String number, LocalDate creationDate, double balance, Client client,AccountType accountType) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.client = client;
        this.accountActive = true;
        this.accountType= accountType;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();

    public long getId() {
        return id;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public LocalDate getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {this.balance = balance;}
    @JsonIgnore
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public boolean isAccountActive() {return accountActive;}

    public void setAccountActive(boolean accountActive) {this.accountActive = accountActive;}

    public void addTransaction(Transaction transaction){transaction.setAccount(this);transactions.add(transaction);}

    public AccountType getAccountType() {return accountType;}

    public void setAccountType(AccountType accountType) {this.accountType = accountType;}
}
