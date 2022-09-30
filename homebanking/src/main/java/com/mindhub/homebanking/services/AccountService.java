package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;

import java.util.List;

public interface AccountService {
    public List<Account> getAllAccounts ();
    Account getAccountById(Long id);
    Account getClientByNumber(String number);
    void saveAccount(Account account);
}
