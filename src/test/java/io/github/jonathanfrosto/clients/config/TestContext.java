package io.github.jonathanfrosto.clients.config;

import io.github.jonathanfrosto.clients.mappers.ClientMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestContext {

    @Bean
    public ClientMapper clientMapper() {
        return Mappers.getMapper(ClientMapper.class);
    }
}
