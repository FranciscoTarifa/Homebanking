package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface ClientService {
    public List<Client> getAllClients();
    Client getClientById (Long id);
    Client findClientByEmail(String email);
    void saveClient (Client client);
}
