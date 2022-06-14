package io.github.jonathanfrosto.clients.config.db;

import io.github.jonathanfrosto.clients.entities.Client;
import io.github.jonathanfrosto.clients.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("pre-persist")
public class PrePersist implements CommandLineRunner {

    private final ClientRepository clientRepository;

    public PrePersist(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void run(String... args) {
        Client client = new Client();
        client.setName("Jonathan");
        client.setCpf("70582818400");
        clientRepository.save(client);
    }
}
