package com.ada.banco.domain.gateway;

import com.ada.banco.domain.model.Conta;
import com.ada.banco.domain.model.ClientTransaction;

import java.util.List;

public interface TransactionGateway {
    List<ClientTransaction> getTransactionsBySourceAccount(Conta sourceAccount);

    List<ClientTransaction> getTransactionsByDestinationAccount(Conta destinationAccount);
    ClientTransaction saveNewTransaction(ClientTransaction clientTransaction);
}
