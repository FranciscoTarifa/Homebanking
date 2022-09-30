package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;

import java.time.LocalDateTime;
import java.util.Set;

public interface TransactionService {
    void saveTransactions(Transaction transaction);

    public Set<Transaction> filterTransactionWithDate(LocalDateTime fromDate, LocalDateTime toDate, Account account);
}
