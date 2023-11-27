package com.ada.banco.infra.controller;

import com.ada.banco.domain.model.Conta;
import com.ada.banco.domain.usecase.CreateNewAccount;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contas")
public class ContaController {

    private CreateNewAccount createNewAccount;

    public ContaController(CreateNewAccount createNewAccount) {
        this.createNewAccount = createNewAccount;
    }

    @PostMapping
    public ResponseEntity criarConta(@RequestBody Conta conta) throws Exception {
        Conta novaConta;
        try {
            novaConta = createNewAccount.execute(conta);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);
    }
}
