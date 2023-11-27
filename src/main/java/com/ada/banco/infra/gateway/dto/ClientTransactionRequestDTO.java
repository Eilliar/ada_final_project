package com.ada.banco.infra.gateway.dto;


import com.ada.banco.domain.model.ClientTransaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ClientTransactionRequestDTO {

    private ClientTransaction.ClientTransactionType clientTransactionType;
    private String sourceAccountCpf;
    private String destinationAccountCpf;
    private BigDecimal clientTransactionValue;

    public ClientTransactionRequestDTO(ClientTransaction.ClientTransactionType clientTransactionType, String sourceAccountCpf, String destinationAccountCpf, BigDecimal clientTransactionValue) {
        this.clientTransactionType = clientTransactionType;
        this.sourceAccountCpf = sourceAccountCpf;
        this.destinationAccountCpf = destinationAccountCpf;
        this.clientTransactionValue = clientTransactionValue;
    }

    public ClientTransaction.ClientTransactionType getClientTransactionType() {
        return clientTransactionType;
    }

    public String getSourceAccountCpf() {
        return sourceAccountCpf;
    }

    public String getDestinationAccountCpf() {
        return destinationAccountCpf;
    }

    public BigDecimal getClientTransactionValue() {
        return clientTransactionValue;
    }
}
