package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    Set<Account> accounts = new HashSet<>();
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();

    public Client (){

    }
    public Client(String firstName, String lastName, String email,String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
    public void setClientLoans(Set<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }
    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }
    public long getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Account> getAccounts(){return accounts;} //con el set nunca vamos a tener 2 cosas iguales dentro del mismo.

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public void setAccounts(Set<Account> accounts) {this.accounts = accounts;}
    public Set<Card> getCards() {return cards;}

    public void setCards(Set<Card> cards) {this.cards = cards;}

    public void addAccount(Account account){
        account.setClient(this);
        accounts.add(account);
    }
    public void addCard(Card card){
        card.setClient(this);
        cards.add(card);
    }

}
