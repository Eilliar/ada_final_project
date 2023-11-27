package com.ada.banco.domain.usecase;

import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.gateway.EmailGateway;
import com.ada.banco.domain.gateway.TransactionGateway;
import com.ada.banco.domain.model.ClientTransaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CreateNewTransaction {
    private TransactionGateway transactionGateway;
    private ContaGateway contaGateway;
    private EmailGateway emailGateway;


    public CreateNewTransaction(TransactionGateway transactionGateway, ContaGateway contaGateway, EmailGateway emailGateway) {
        this.transactionGateway = transactionGateway;
        this.contaGateway = contaGateway;
        this.emailGateway = emailGateway;
    }

    public ClientTransaction execute(ClientTransaction clientTransaction) throws Exception {
        if (clientTransaction.getClientTransactionValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("Transaction value must be greater than 0.");
        }

        if (clientTransaction.getClientTransactionType() == ClientTransaction.ClientTransactionType.DEPOSITO &&
                contaGateway.buscarPorCpf(clientTransaction.getDestinationAccount().getCpf()) == null) {
            throw new Exception("Destination account does not exist.");
        }

        if (clientTransaction.getClientTransactionType() == ClientTransaction.ClientTransactionType.SAQUE &&
                contaGateway.buscarPorCpf(clientTransaction.getSourceAccount().getCpf()) == null) {
            throw new Exception("Source account does not exist.");
        }

        if (clientTransaction.getClientTransactionType() == ClientTransaction.ClientTransactionType.TRANSFERENCIA &&
                (contaGateway.buscarPorCpf(clientTransaction.getDestinationAccount().getCpf()) == null ||
                        contaGateway.buscarPorCpf(clientTransaction.getSourceAccount().getCpf()) == null))
            throw new Exception("Destination/Source account does not exist.");


        updateAccountBalance(clientTransaction);
        ClientTransaction newClientTransaction = transactionGateway.saveNewTransaction(clientTransaction);
        sendEmail(clientTransaction);
        return newClientTransaction;

    }

    public void updateAccountBalance(ClientTransaction clientTransaction) {

    }

    public void sendEmail(ClientTransaction clientTransaction) {
        if (clientTransaction.getClientTransactionType() == ClientTransaction.ClientTransactionType.DEPOSITO ||
                clientTransaction.getClientTransactionType() == ClientTransaction.ClientTransactionType.TRANSFERENCIA) {
            emailGateway.send(clientTransaction.getDestinationAccount().getCpf());
        }
        if (clientTransaction.getClientTransactionType() == ClientTransaction.ClientTransactionType.SAQUE ||
                clientTransaction.getClientTransactionType() == ClientTransaction.ClientTransactionType.TRANSFERENCIA) {
            emailGateway.send(clientTransaction.getSourceAccount().getCpf());
        }
    }


}
