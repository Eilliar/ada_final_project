package com.ada.banco.infra.controller;

import com.ada.banco.domain.model.Client;
import com.ada.banco.domain.usecase.CreateNewClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class ClientController {
    private CreateNewClient createNewClient;

    public ClientController(CreateNewClient createNewClient){
        this.createNewClient = createNewClient;
    }

    @PostMapping
    public ResponseEntity createNewUser(@RequestBody Client client) throws Exception{
        Client newClient;

        try {
            newClient = createNewClient.execute(client);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(newClient);
    }
}
