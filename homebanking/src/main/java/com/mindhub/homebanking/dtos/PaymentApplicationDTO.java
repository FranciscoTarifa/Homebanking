package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Transaction;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PaymentApplicationDTO {
    private long id;
    private String cardNumber;
    private int cardCvv;
    private double amount;
    private String description;
    private LocalDateTime thruDate;
    private String cardHolder;
    private String number;

    public PaymentApplicationDTO() {
    }

    public PaymentApplicationDTO(Card card,Transaction transaction,Account account) {
        this.cardNumber = card.getNumber();
        this.cardCvv = card.getCvv();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.thruDate = card.getThruDate();
        this.cardHolder = card.getCardholder();
        this.number = account.getNumber();
    }

    public long getId() {return id;}

    public String getCardNumber() {return cardNumber;}

    public int getCardCvv() {return cardCvv;}

    public double getAmount() {return amount;}

    public String getDescription() {return description;}

    public LocalDateTime getThruDate() {return thruDate;}

    public String getCardHolder() {return cardHolder;}

    public String getNumber() {return number;}
}
