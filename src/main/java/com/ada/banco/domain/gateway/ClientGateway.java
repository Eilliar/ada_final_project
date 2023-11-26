package com.ada.banco.domain.gateway;

import com.ada.banco.domain.model.Client;

public interface ClientGateway {
    Client searchByCpf(String cpf);
    Client saveNewClient(Client client);
}
