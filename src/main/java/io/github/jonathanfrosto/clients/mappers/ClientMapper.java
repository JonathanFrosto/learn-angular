package io.github.jonathanfrosto.clients.mappers;

import io.github.jonathanfrosto.clients.dto.ClientDTO;
import io.github.jonathanfrosto.clients.entities.Client;
import org.mapstruct.Mapper;

@Mapper
public interface ClientMapper {
    Client dtoToEntity(ClientDTO clientDTO);
    ClientDTO entityToDto(Client save);
}
