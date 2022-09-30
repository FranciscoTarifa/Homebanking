package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {
    Loan findByName(String name);
    public List<Loan> getAllLoans();
}
