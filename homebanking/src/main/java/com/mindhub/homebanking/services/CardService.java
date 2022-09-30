package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;

public interface CardService{
    Card getCardByNumber (String number);
    void saveCard (Card card);
}
