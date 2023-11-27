package com.ada.banco.infra.controller;

import com.ada.banco.domain.model.ClientTransaction;
import com.ada.banco.domain.model.Conta;
import com.ada.banco.infra.gateway.bd.ContaRepository;
import com.ada.banco.infra.gateway.bd.TransactionRepository;
import com.ada.banco.infra.gateway.dto.ClientTransactionRequestDTO;
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

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientTransactionControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContaRepository contaRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ClientTransactionController clientTransactionController;

    @Test
    @Transactional
    void throwExceptionWhenAccountDoesNotExist() throws Exception {
        ClientTransactionRequestDTO requestDTO = new ClientTransactionRequestDTO(ClientTransaction.ClientTransactionType.SAQUE,
                "30941720081", "30941720081", BigDecimal.valueOf(5000));
        String requestBody = objectMapper.writeValueAsString(requestDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/transactions")
                .contentType(MediaType.APPLICATION_JSON).content(requestBody)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Transactional
    void shouldDepositToClientAccountAndReturn201() throws Exception {
        Conta account =
                new Conta(4242L, 42L, Conta.AccountType.CORRENTE, BigDecimal.ZERO, "Bruno", "30941720080");
        contaRepository.save(account);

        ClientTransactionRequestDTO requestDTO = new ClientTransactionRequestDTO(ClientTransaction.ClientTransactionType.DEPOSITO,
                "30941720080", "30941720080", BigDecimal.valueOf(5000));

        String requestBody = objectMapper.writeValueAsString(requestDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/transactions")
                .contentType(MediaType.APPLICATION_JSON).content(requestBody)
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        List<ClientTransaction> transactions = transactionRepository.findBySourceAccount(account);
        Assertions.assertNotNull(transactions);
    }

}
