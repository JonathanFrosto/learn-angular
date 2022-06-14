package io.github.jonathanfrosto.clients.controllers;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jonathanfrosto.clients.dto.ClientDTO;
import io.github.jonathanfrosto.clients.entities.Client;
import io.github.jonathanfrosto.clients.errors.exceptions.BadRequestException;
import io.github.jonathanfrosto.clients.errors.exceptions.NotFoundException;
import io.github.jonathanfrosto.clients.mappers.ClientMapper;
import io.github.jonathanfrosto.clients.services.ClientService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class ClientControllerTest {

    private static final Schema CLIENT_DTO = Schema.schema("ClientDTO");
    private static final Long id = 1L;
    private static final String name = "Jonathan";
    private static final String cpf = "611.206.180-53";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ClientService clientService;

    @MockBean
    private ClientMapper clientMapper;

    @Test
    void saveClient() throws Exception {
        ClientDTO validDTO = getValidDTO();
        Client validEntity = getValidEntity();

        when(clientMapper.dtoToEntity(any(ClientDTO.class))).thenReturn(validEntity);
        when(clientService.save(validEntity)).thenReturn(validEntity);
        when(clientMapper.entityToDto(validEntity)).thenReturn(validDTO);

        String content = mapper.writeValueAsString(validDTO);
        mockMvc.perform(postRequest(content))
                .andDo(document("SaveClient", resource(
                        ResourceSnippetParameters.builder()
                                .summary("Save a Client")
                                .description("Register a new client")
                                .requestFields(clientDTOFields())
                                .requestSchema(CLIENT_DTO)
                                .responseFields(clientDTOFields())
                                .responseSchema(CLIENT_DTO)
                                .build()
                )))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(validDTO.getName())))
                    .andExpect(jsonPath("$.cpf", is(validDTO.getCpf())));
    }

    @Test
    void saveInvalidDTO() throws Exception {
        ClientDTO validDTO = new ClientDTO();
        String content = mapper.writeValueAsString(validDTO);

        mockMvc.perform(postRequest(content))
                .andDo(document("SaveClient - 400 - Invalid DTO", resource()))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void savePassingId() throws Exception {
        ClientDTO validDTO = getValidDTO();

        String content = mapper.writeValueAsString(validDTO);

        when(clientMapper.dtoToEntity(any(ClientDTO.class))).thenReturn(getValidEntity());
        when(clientService.save(any(Client.class))).thenThrow(new BadRequestException("forbidden.id"));

        mockMvc.perform(postRequest(content))
                .andDo(document("SaveClient - 400 - Passing ID", resource()))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException));
    }

    @Test
    void getClient() throws Exception {
        ClientDTO clientDTO = getValidDTO();
        Client client = getValidEntity();

        when(clientService.getById(id)).thenReturn(client);
        when(clientMapper.entityToDto(client)).thenReturn(clientDTO);

        mockMvc.perform(get("/clients/{id}", id))
                .andDo(document("FindClient", resource(
                        ResourceSnippetParameters.builder()
                                .summary("Find a client")
                                .description("Find a client registered")
                                .pathParameters(
                                        parameterWithName("id").description("Custumer identifier")
                                )
                                .responseFields(clientDTOFields())
                                .responseSchema(CLIENT_DTO)
                                .build()
                )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.cpf", is(cpf)));
    }

    @Test
    void notFoundClient() throws Exception {
        Long id = 2L;

        when(clientService.getById(id)).thenThrow(new NotFoundException("client.notfound"));

        mockMvc.perform(get("/clients/{id}", id))
                .andDo(document("FindClient - 404", resource()))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateClient() throws Exception {
        ClientDTO validDTO = getValidDTO();
        Client validEntity = getValidEntity();

        when(clientMapper.dtoToEntity(any(ClientDTO.class))).thenReturn(validEntity);
        when(clientService.update(validEntity)).thenReturn(validEntity);
        when(clientMapper.entityToDto(validEntity)).thenReturn(validDTO);

        String content = mapper.writeValueAsString(validDTO);
        mockMvc.perform(putRequest(content))
                .andDo(document("UpdateClient", resource(
                        ResourceSnippetParameters.builder()
                                .summary("Update a Client")
                                .description("Update an existing client")
                                .requestFields(clientDTOFields())
                                .requestSchema(CLIENT_DTO)
                                .responseFields(clientDTOFields())
                                .responseSchema(CLIENT_DTO)
                                .build()
                )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(validDTO.getName())))
                .andExpect(jsonPath("$.cpf", is(validDTO.getCpf())));
    }

    @Test
    void updateNotFoundClient() throws Exception {
        Client validEntity = getValidEntity();
        String content = mapper.writeValueAsString(getValidDTO());

        when(clientMapper.dtoToEntity(any(ClientDTO.class))).thenReturn(validEntity);
        when(clientService.update(validEntity)).thenThrow(new NotFoundException("client.notfound"));

        mockMvc.perform(putRequest(content))
                .andDo(document("UpdateClient - 404", resource()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteClient() throws Exception {
        mockMvc.perform(delete("/clients/{id}", id))
                .andDo(document("DeleteClient", resource(
                        ResourceSnippetParameters.builder()
                                .summary("Delete a Client")
                                .description("Delete an existing client")
                                .pathParameters(
                                        parameterWithName("id").description("Custumer identifier")
                                )
                                .build()
                )))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteNotFound() throws Exception {
        long id = 2L;

        doThrow(new NotFoundException("client.notfound")).when(clientService).delete(id);

        mockMvc.perform(delete("/clients/{id}", id))
                .andDo(document("DeleteClient - 404", resource(
                        ResourceSnippetParameters.builder()
                                .build()
                )))
                .andExpect(status().isNotFound());
    }

    @NotNull
    private ClientDTO getValidDTO() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(id);
        clientDTO.setName(name);
        clientDTO.setCpf(cpf);
        return clientDTO;
    }

    @NotNull
    private Client getValidEntity() {
        Client client = new Client();
        client.setId(id);
        client.setName(name);
        client.setCpf(cpf);
        return client;
    }

    @NotNull
    private MockHttpServletRequestBuilder postRequest(String content) {
        return post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
    }

    @NotNull
    private MockHttpServletRequestBuilder putRequest(String content) {
        return put("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
    }

    private FieldDescriptor[] clientDTOFields() {
        return new FieldDescriptor[]{
                fieldWithPath("id").description("Client identifier"),
                fieldWithPath("name").description("Client name"),
                fieldWithPath("cpf").description("Client brazilian identifier")};
    }
}