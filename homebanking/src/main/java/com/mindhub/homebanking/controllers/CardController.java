package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.CardsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    CardService cardService;
    @Autowired
    ClientService clientService;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object>createCard(
            @RequestParam CardColor cardColor, @RequestParam CardType cardType,
            Authentication authentication
            ){
        Client client = clientService.findClientByEmail(authentication.getName());
        List <Card> cardListActive = client.getCards().stream().filter(card -> card.isCardActive()).collect(Collectors.toList());
        if (cardListActive.stream().filter(card -> card.getCardtype().equals(cardType)).filter(card -> card.getCardcolor().equals(cardColor)).count()>0){
            return new ResponseEntity<>("You have the maximum of the selected card", HttpStatus.FORBIDDEN);
        }
        if (cardListActive.stream().filter(card -> card.getCardtype().equals(cardType)).count()>=3){
            return new ResponseEntity<>("Clients can only have 3 cards of each type.", HttpStatus.FORBIDDEN);
        }else {
            int cvvRandomNumber = CardsUtils.getCcvRandomNumber();
            String randomCardNumber = CardsUtils.getRandomCardNumber();
            while (cardService.getCardByNumber(randomCardNumber)!=null){
                randomCardNumber = CardsUtils.getRandomCardNumber();
            };
            Card card = new Card(client,client.getFirstName()+" "+client.getLastName(),cardType,cardColor,randomCardNumber,cvvRandomNumber,LocalDateTime.now().plusYears(5),LocalDateTime.now());
            cardService.saveCard(card);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @PatchMapping("/clients/current/cards")
    public ResponseEntity <Object> deleteCard(
            @RequestParam String number, Authentication authentication
    ){
        Client client = clientService.findClientByEmail(authentication.getName());
        Card card = cardService.getCardByNumber(number);
        if (number.isEmpty()){
            return new ResponseEntity<>("Please select card", HttpStatus.FORBIDDEN);
        }
        if (!client.getCards().contains(card)){
            return new ResponseEntity<>("This card is not yours",HttpStatus.FORBIDDEN);
        }
        card.setCardActive(false);
        cardService.saveCard(card);
        return new ResponseEntity<>("Card disable", HttpStatus.ACCEPTED);
    }
}