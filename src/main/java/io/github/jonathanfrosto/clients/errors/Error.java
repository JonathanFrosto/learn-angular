package io.github.jonathanfrosto.clients.errors;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Error {

    private String path;
    private String method;
    private String description;
}
