package com.ada.banco.infra.gateway.bd;

import java.util.List;
import com.ada.banco.domain.model.Conta;
import com.ada.banco.domain.model.ClientTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<ClientTransaction, Long> {
    List<ClientTransaction> findBySourceAccount(Conta sourceAccount);
    List<ClientTransaction> findByDestinationAccount(Conta destinationAccount);
}
