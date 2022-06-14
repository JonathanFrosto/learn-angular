package io.github.jonathanfrosto.clients.services;

import io.github.jonathanfrosto.clients.entities.Client;

public interface ClientService {
    Client save(Client client);

    Client getById(Long id);

    Client update(Client newInformationClient);

    void delete(Long id);
}
