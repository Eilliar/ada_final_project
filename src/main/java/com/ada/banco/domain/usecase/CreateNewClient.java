package com.ada.banco.domain.usecase;

import com.ada.banco.domain.gateway.EmailGateway;
import com.ada.banco.domain.gateway.ClientGateway;
import com.ada.banco.domain.model.Client;
import org.springframework.stereotype.Component;

@Component
public class CreateNewClient {

    private ClientGateway clientGateway;
    private EmailGateway emailGateway;

    public CreateNewClient(ClientGateway clientGateway, EmailGateway emailGateway){
        this.clientGateway = clientGateway;
        this.emailGateway = emailGateway;
    }

    public Client execute(Client client) throws Exception {
        if(clientGateway.searchByCpf(client.getCpf()) != null){
            throw new Exception("User already exists.");
        }

        Client newClient = clientGateway.saveNewClient(client);
        emailGateway.send(newClient.getCpf());
        return newClient;
    }
}
