package io.github.jonathanfrosto.clients.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ServiceProvidedDTO {

    private Long id;
    private String description;
    private BigDecimal value;
    private ClientDTO client;
}
