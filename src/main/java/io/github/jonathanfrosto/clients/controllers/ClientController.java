package io.github.jonathanfrosto.clients.controllers;

import io.github.jonathanfrosto.clients.dto.ClientDTO;
import io.github.jonathanfrosto.clients.entities.Client;
import io.github.jonathanfrosto.clients.mappers.ClientMapper;
import io.github.jonathanfrosto.clients.services.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClientController {

    private final ClientMapper mapper;
    private final ClientService clientService;

    public ClientController(ClientMapper mapper, ClientService clientService) {
        this.mapper = mapper;
        this.clientService = clientService;
    }

    @PostMapping("/clients")
    @ResponseStatus(HttpStatus.CREATED)
    public ClientDTO createClient(@Validated @RequestBody ClientDTO clientDTO) {
        Client client = mapper.dtoToEntity(clientDTO);
        return mapper.entityToDto(clientService.save(client));
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        return mapper.entityToDto(clientService.getById(id));
    }

    @PutMapping("/clients")
    public ClientDTO updateClient(@RequestBody ClientDTO clientDTO) {
        Client newInformationClient = mapper.dtoToEntity(clientDTO);
        return mapper.entityToDto(clientService.update(newInformationClient));
    }

    @DeleteMapping("/clients/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClient(@PathVariable Long id) {
        clientService.delete(id);
    }
}
