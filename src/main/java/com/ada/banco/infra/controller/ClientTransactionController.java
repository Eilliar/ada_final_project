package com.ada.banco.infra.controller;

import com.ada.banco.domain.model.ClientTransaction;
import com.ada.banco.domain.model.Conta;
import com.ada.banco.domain.usecase.CreateNewTransaction;
import com.ada.banco.infra.gateway.bd.ContaGatewayDatabase;
import com.ada.banco.infra.gateway.dto.ClientTransactionRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
public class ClientTransactionController {
    private CreateNewTransaction createNewTransaction;
    private ContaGatewayDatabase contaGatewayDatabase;

    public ClientTransactionController(CreateNewTransaction createNewTransaction, ContaGatewayDatabase contaGatewayDatabase) {
        this.createNewTransaction = createNewTransaction;
        this.contaGatewayDatabase = contaGatewayDatabase;
    }

    @PostMapping
    public ResponseEntity createTransaction(@RequestBody ClientTransactionRequestDTO requestBody) throws Exception {
        ClientTransaction newClientTransaction;
        Conta sourceAccount;
        Conta destinationAccount;

        try {
            sourceAccount = this.contaGatewayDatabase.buscarPorCpf(requestBody.getSourceAccountCpf());
            destinationAccount = this.contaGatewayDatabase.buscarPorCpf(requestBody.getDestinationAccountCpf());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        ClientTransaction clientTransaction = new ClientTransaction(
                requestBody.getClientTransactionType(),
                sourceAccount,
                destinationAccount,
                requestBody.getClientTransactionValue()
        );

        try {
            newClientTransaction = this.createNewTransaction.execute(clientTransaction);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(newClientTransaction);
    }
}
