package com.ada.banco.domain.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Conta {

    public enum AccountType {
        CORRENTE,
        POUPANCA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long agencia;
    private Long digito;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private BigDecimal saldo;
    private String titular;
    @Column(unique = true)
    private String cpf;

    public Conta() {
    }

    public Conta(Long id, Long agencia, Long digito, AccountType accountType, BigDecimal saldo, String titular, String cpf) {
        this.id = id;
        this.agencia = agencia;
        this.digito = digito;
        this.accountType = accountType;
        this.saldo = saldo;
        this.titular = titular;
        this.cpf = cpf;
    }

    public Conta(Long agencia, Long digito, AccountType accountType, BigDecimal saldo, String titular, String cpf) {
        this.agencia = agencia;
        this.digito = digito;
        this.accountType = accountType;
        this.saldo = saldo;
        this.titular = titular;
        this.cpf = cpf;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAgencia() {
        return agencia;
    }

    public void setAgencia(Long agencia) {
        this.agencia = agencia;
    }

    public Long getDigito() {
        return digito;
    }

    public void setDigito(Long digito) {
        this.digito = digito;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
