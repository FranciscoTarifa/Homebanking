package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {
    private long clientLoanId;
    private long loanId;
    private String loanName;
    private double amount;
    private int payments;

    public ClientLoanDTO(long clientLoanId) {
    }

    public ClientLoanDTO(ClientLoan clientloan) {
        this.clientLoanId = clientloan.getId();
        this.loanId = clientloan.getLoan().getId();
        this.loanName =clientloan.getLoan().getName();
        this.amount = clientloan.getAmount();
        this.payments = clientloan.getPayments();
    }

    public long getClientLoanId() {
        return clientLoanId;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPayments() {
        return payments;
    }

    public void setPayments(int payments) {
        this.payments = payments;
    }
}
