package com.ada.banco.infra.controller;

import com.ada.banco.domain.model.Client;
import com.ada.banco.infra.gateway.bd.ClientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientController clientController;

    @Test
    @Transactional
    void successfullyCreateNewClientReturning201() throws Exception {
        String requestBody = objectMapper.writeValueAsString(
                new Client("Bruno", "Godoi Eilliar", "30941720080")
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        // Make sure it saved on DB
        Client createdClient = clientRepository.findByCpf("30941720080");
        Assertions.assertNotNull(createdClient);

        Assertions.assertAll(
                () -> Assertions.assertEquals("Bruno", createdClient.getFirstName()),
                () -> Assertions.assertEquals("Godoi Eilliar", createdClient.getLastName()),
                () -> Assertions.assertEquals("30941720080", createdClient.getCpf())
        );
    }

    @Test
    @Transactional
    void throwExceptionWhenNewUserAlreadyExists() throws Exception {

        Client client = new Client("Bruno", "Godoi Eilliar", "30941720080");
        String requestBody = objectMapper.writeValueAsString(client);
        // Make sure to create client, so that it exists in DB
        clientRepository.save(client);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
