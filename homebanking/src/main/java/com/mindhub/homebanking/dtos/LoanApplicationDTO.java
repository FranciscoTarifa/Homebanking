package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

public class LoanApplicationDTO {
    private long idLoan;
    private double amount;
    private String loanName;
    private Integer payments;
    private String accountNumberTo;

    public LoanApplicationDTO() {
    }

    public LoanApplicationDTO(ClientLoan clientLoan,Loan loan, Account account) {
        this.idLoan = loan.getId();
        this.amount = clientLoan.getAmount();
        this.loanName = loan.getName();
        this.payments = clientLoan.getPayments();
        this.accountNumberTo = account.getNumber();
    }

    public long getIdLoan() {return idLoan;}

    public double getAmount() {return amount;}

    public Integer getPayments() {return payments;}

    public String getAccountNumberTo() {return accountNumberTo;}

    public String getloanName() {return loanName;}
}
