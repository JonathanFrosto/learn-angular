package io.github.jonathanfrosto.clients.services.impl;

import io.github.jonathanfrosto.clients.errors.exceptions.BadRequestException;
import io.github.jonathanfrosto.clients.errors.exceptions.NotFoundException;
import io.github.jonathanfrosto.clients.entities.Client;
import io.github.jonathanfrosto.clients.repositories.ClientRepository;
import io.github.jonathanfrosto.clients.services.ClientService;
import org.springframework.stereotype.Service;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.github.jonathanfrosto.clients.errors.ErrorMessages.CLIENT_NOT_FOUND;
import static io.github.jonathanfrosto.clients.util.NullSafer.consumeIfNotNull;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client save(Client client) {
        if (client.getId() != null) {
            throw new BadRequestException("forbidden.id");
        }

        return clientRepository.save(client);
    }

    @Override
    public Client getById(Long id) {
        return clientRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(CLIENT_NOT_FOUND));
    }

    @Override
    public Client update(Client newInformationClient) {
        Client toUpdateClient = clientRepository
                .findById(newInformationClient.getId())
                .orElseThrow(() -> new NotFoundException(CLIENT_NOT_FOUND));

        consumeIfNotNull(newInformationClient.getName(), toUpdateClient::setName);
        consumeIfNotNull(newInformationClient.getCpf(), toUpdateClient::setCpf);

        return clientRepository.save(toUpdateClient);
    }

    @Override
    public void delete(Long id) {
        Client client = clientRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(CLIENT_NOT_FOUND));

        clientRepository.delete(client);
    }
}
