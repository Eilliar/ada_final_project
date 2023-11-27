package com.ada.banco.domain.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class ClientTransaction {

    public enum ClientTransactionType {
        DEPOSITO,
        SAQUE,
        TRANSFERENCIA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ClientTransactionType clientTransactionType;
    @ManyToOne
    @JoinColumn(name = "source_account_id")
    private Conta sourceAccount;
    @ManyToOne
    @JoinColumn(name = "destination_account_id")
    private Conta destinationAccount;
    private BigDecimal clientTransactionValue;
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime clientTransactionDatetime;

    public ClientTransaction() {
    }

    public ClientTransaction(Long id, ClientTransactionType clientTransactionType, Conta sourceAccount, Conta destinationAccount, BigDecimal ClientTransactionValue) {
        this.id = id;
        this.clientTransactionType = clientTransactionType;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.clientTransactionValue = ClientTransactionValue;
        this.clientTransactionDatetime = LocalDateTime.now();
    }

    public ClientTransaction(ClientTransactionType clientTransactionType, Conta sourceAccount, Conta destinationAccount, BigDecimal ClientTransactionValue) {
        this.clientTransactionType = clientTransactionType;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.clientTransactionValue = ClientTransactionValue;
        this.clientTransactionDatetime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClientTransactionType getClientTransactionType() {
        return clientTransactionType;
    }

    public void setClientTransactionType(ClientTransactionType clientTransactionType) {
        this.clientTransactionType = clientTransactionType;
    }

    public Conta getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(Conta sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public Conta getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(Conta destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public BigDecimal getClientTransactionValue() {
        return clientTransactionValue;
    }

    public void setClientTransactionValue(BigDecimal clientTransactionValue) {
        this.clientTransactionValue = clientTransactionValue;
    }

    public LocalDateTime getClientTransactionDatetime() {
        return clientTransactionDatetime;
    }

    public void setClientTransactionDatetime(LocalDateTime clientTransactionDatetime) {
        this.clientTransactionDatetime = clientTransactionDatetime;
    }
}
