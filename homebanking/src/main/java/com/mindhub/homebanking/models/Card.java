package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "card_id")
    private Client client;
    private String cardholder;
    private CardType cardtype;
    private CardColor cardcolor;
    private String number;
    private Integer cvv;
    private LocalDateTime thruDate;
    private LocalDateTime fromDate;
    private boolean cardActive;

    public Card() {
    }

    public Card(Client client, CardType cardtype, CardColor cardcolor, String number, Integer cvv, LocalDateTime thruDate, LocalDateTime fromDate) {
        this.cardholder = (client.getFirstName()+" "+client.getLastName());
        this.cardtype = cardtype;
        this.cardcolor = cardcolor;
        this.number = number;
        this.cvv = cvv;
        this.thruDate = thruDate;
        this.fromDate = fromDate;
        this.cardActive = true;
    }

    public Card(Client client, String cardholder, CardType cardtype, CardColor cardcolor, String number, Integer cvv, LocalDateTime thruDate, LocalDateTime fromDate) {
        this.client = client;
        this.cardholder = cardholder;
        this.cardtype = cardtype;
        this.cardcolor = cardcolor;
        this.number = number;
        this.cvv = cvv;
        this.thruDate = thruDate;
        this.fromDate = fromDate;
        this.cardActive = true;
    }

    public long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getCardholder() {
        return cardholder;
    }

    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }

    public CardType getCardtype() {
        return cardtype;
    }

    public void setCardtype(CardType cardtype) {
        this.cardtype = cardtype;
    }

    public CardColor getCardcolor() {
        return cardcolor;
    }

    public void setCardcolor(CardColor cardcolor) {
        this.cardcolor = cardcolor;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getCvv() {
        return cvv;
    }

    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }

    public LocalDateTime getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDateTime thruDate) {
        this.thruDate = thruDate;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public boolean isCardActive() {
        return cardActive;
    }

    public void setCardActive(boolean cardActive) {
        this.cardActive = cardActive;
    }
}

