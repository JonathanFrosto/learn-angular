package io.github.jonathanfrosto.clients.mappers;

import io.github.jonathanfrosto.clients.dto.ServiceProvidedDTO;
import io.github.jonathanfrosto.clients.entities.ServiceProvided;
import org.mapstruct.Mapper;

@Mapper
public interface ServiceProvidedMapper {

    ServiceProvided dtoToEntity(ServiceProvidedDTO serviceProvidedDTO);
}
