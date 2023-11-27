package com.ada.banco.infra.gateway.bd;

import com.ada.banco.domain.gateway.TransactionGateway;
import com.ada.banco.domain.model.Conta;
import com.ada.banco.domain.model.ClientTransaction;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionGatewayDatabase implements TransactionGateway {

    TransactionRepository transactionRepository;

    public TransactionGatewayDatabase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<ClientTransaction> getTransactionsBySourceAccount(Conta sourceAccount) {
        return transactionRepository.findBySourceAccount(sourceAccount);
    }

    @Override
    public List<ClientTransaction> getTransactionsByDestinationAccount(Conta destinationAccount) {
        return transactionRepository.findByDestinationAccount(destinationAccount);
    }

    @Override
    public ClientTransaction saveNewTransaction(ClientTransaction clientTransaction) {
        return transactionRepository.save(clientTransaction);
    }
}
