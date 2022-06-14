package io.github.jonathanfrosto.clients.repositories;

import io.github.jonathanfrosto.clients.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
