package io.github.jonathanfrosto.clients.controllers;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.MockMvc;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(Testinho.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class TestinhoTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void printaoQUEeUquero() throws Exception {

        mockMvc.perform(RestDocumentationRequestBuilders.post("/testinho"))
                .andDo(document("Test", resource(
                        ResourceSnippetParameters.builder()
                                .summary("Test")
                                .description("Test")
                                .responseFields(
                                        fieldWithPath("totalElements").description("Número total de elementos"),
                                        fieldWithPath("elements").description("Elementos"),
                                        fieldWithPath("elements[].nome").description("nome da pessoa"),
                                        fieldWithPath("elements[].idade").description("idade da pessoa")
                                )
                                .build()
                )))
                .andExpect(status().isOk());
    }
}
