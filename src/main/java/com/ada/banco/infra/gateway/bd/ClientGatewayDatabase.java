package com.ada.banco.infra.gateway.bd;

import com.ada.banco.domain.gateway.ClientGateway;
import com.ada.banco.domain.model.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientGatewayDatabase implements ClientGateway {
    ClientRepository clientRepository;

    public ClientGatewayDatabase(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }

    @Override
    public Client searchByCpf(String cpf){
        return clientRepository.findByCpf(cpf);
    }

    @Override
    public Client saveNewUser(Client client){
        return clientRepository.save(client);
    }
}
