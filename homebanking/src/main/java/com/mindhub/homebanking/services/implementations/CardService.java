package com.mindhub.homebanking.services.implementations;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService implements com.mindhub.homebanking.services.CardService {
    @Autowired
    CardRepository cardRepository;
    @Override
    public Card getCardByNumber(String number) {
        return cardRepository.findByNumber(number);
    }
    @Override
    public void saveCard(Card card) {
    cardRepository.save(card);
    }
}
