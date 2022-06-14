package io.github.jonathanfrosto.clients.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ClientDTO {

    private Long id;

    @NotEmpty(message = "{field.not.empty}")
    private String name;

    @NotNull(message = "{field.not.null}")
    private String cpf;
}
