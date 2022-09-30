package com.mindhub.homebanking.dtos;

import java.time.LocalDateTime;

public class FilteredTransactionsDTO {
    public LocalDateTime fromDate;
    public LocalDateTime toDate;
    public String accountNumber;

    public FilteredTransactionsDTO() {}

    public FilteredTransactionsDTO(LocalDateTime fromDate, LocalDateTime toDate, String accountNumber) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.accountNumber = accountNumber;
    }

    public LocalDateTime getFromDate() {return fromDate;}

    public LocalDateTime getToDate() {return toDate;}
    public String getAccountNumber() {return accountNumber;}
}
