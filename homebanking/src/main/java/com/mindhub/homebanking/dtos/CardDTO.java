package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CardDTO {
    private long id;
    private String cardholder;
    private CardType cardtype;
    private CardColor cardcolor;
    private String number;
    private Integer cvv;
    private LocalDateTime thruDate;
    private LocalDateTime fromDate;
    private boolean cardActive;

    public CardDTO() {
    }

    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardholder = card.getCardholder();
        this.cardtype = card.getCardtype();
        this.cardcolor = card.getCardcolor();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.thruDate = card.getThruDate();
        this.fromDate = card.getFromDate();
        this.cardActive = card.isCardActive();
    }

    public long getId() {return id;}

    public String getCardholder() {return cardholder;}

    public void setCardholder(String cardholder) {this.cardholder = cardholder;}

    public CardType getCardtype() {return cardtype;}

    public void setCardtype(CardType cardtype) {this.cardtype = cardtype;}

    public CardColor getCardcolor() {return cardcolor;}

    public void setCardcolor(CardColor cardcolor) {this.cardcolor = cardcolor;}

    public String getNumber() {return number;}

    public void setNumber(String number) {this.number = number;}

    public Integer getCvv() {return cvv;}

    public void setCvv(Integer cvv) {this.cvv = cvv;}

    public LocalDateTime getThruDate() {return thruDate;}

    public void setThruDate(LocalDateTime thruDate) {this.thruDate = thruDate;}

    public LocalDateTime getFromDate() {return fromDate;}

    public void setFromDate(LocalDateTime fromDate) {this.fromDate = fromDate;}

    public boolean isCardActive() {return cardActive;}
}

