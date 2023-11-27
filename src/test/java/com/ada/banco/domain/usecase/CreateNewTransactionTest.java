package com.ada.banco.domain.usecase;

import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.gateway.EmailGateway;

import com.ada.banco.domain.gateway.TransactionGateway;
import com.ada.banco.domain.model.ClientTransaction;
import com.ada.banco.domain.model.Conta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static com.ada.banco.domain.model.ClientTransaction.ClientTransactionType.*;


@ExtendWith(MockitoExtension.class)
public class CreateNewTransactionTest {
    @Mock
    private ContaGateway contaGateway;
    @Mock
    private EmailGateway emailGateway;
    @Mock
    private TransactionGateway transactionGateway;
    @InjectMocks
    CreateNewTransaction createNewTransaction;

    @Test
    public void shouldDepositToClientAccount() throws Exception {
        Conta account =
                new Conta(4242L, 42L, Conta.AccountType.CORRENTE, BigDecimal.ZERO, "Bruno", "30941720080");

        ClientTransaction clientTransaction = new ClientTransaction(42L, DEPOSITO,
                account, account, BigDecimal.valueOf(500));

        Mockito.when(contaGateway.buscarPorCpf(account.getCpf())).thenReturn(account);
        Mockito.when(transactionGateway.saveNewTransaction(clientTransaction)).thenReturn(clientTransaction);
        Mockito.doNothing().when(emailGateway).send(account.getCpf());

        ClientTransaction newDeposit = createNewTransaction.execute(clientTransaction);

        Assertions.assertAll(
                () -> Assertions.assertEquals(42L, newDeposit.getId()),
                () -> Assertions.assertEquals(DEPOSITO, newDeposit.getClientTransactionType()),
                () -> Assertions.assertEquals(BigDecimal.valueOf(500), newDeposit.getClientTransactionValue()),
                () -> Assertions.assertEquals("30941720080", newDeposit.getDestinationAccount().getCpf()),
                () -> Assertions.assertEquals("30941720080", newDeposit.getSourceAccount().getCpf())
        );
    }

    @Test
    public void shouldWithdrawFromClientAccount() throws Exception {
        Conta account =
                new Conta(4242L, 42L, Conta.AccountType.CORRENTE, BigDecimal.ZERO, "Bruno", "30941720080");

        ClientTransaction clientTransaction = new ClientTransaction(42L, SAQUE,
                account, account, BigDecimal.valueOf(250));

        Mockito.when(contaGateway.buscarPorCpf(account.getCpf())).thenReturn(account);
        Mockito.when(transactionGateway.saveNewTransaction(clientTransaction)).thenReturn(clientTransaction);
        Mockito.doNothing().when(emailGateway).send(account.getCpf());

        ClientTransaction newDeposit = createNewTransaction.execute(clientTransaction);

        Assertions.assertAll(
                () -> Assertions.assertEquals(42L, newDeposit.getId()),
                () -> Assertions.assertEquals(SAQUE, newDeposit.getClientTransactionType()),
                () -> Assertions.assertEquals(BigDecimal.valueOf(250), newDeposit.getClientTransactionValue()),
                () -> Assertions.assertEquals("30941720080", newDeposit.getDestinationAccount().getCpf()),
                () -> Assertions.assertEquals("30941720080", newDeposit.getSourceAccount().getCpf())
        );
    }

    @Test
    public void shouldTransferBetweenAccounts() throws Exception {
        Conta sourceAccount =
                new Conta(4242L, 42L, Conta.AccountType.CORRENTE, BigDecimal.ZERO, "Bruno", "30941720080");
        Conta destinationAccount =
                new Conta(4243L, 42L, Conta.AccountType.CORRENTE, BigDecimal.ZERO, "Ana", "30941720081");

        ClientTransaction clientTransaction = new ClientTransaction(42L, TRANSFERENCIA,
                sourceAccount, destinationAccount, BigDecimal.valueOf(250));

        Mockito.when(contaGateway.buscarPorCpf(sourceAccount.getCpf())).thenReturn(sourceAccount);
        Mockito.when(contaGateway.buscarPorCpf(destinationAccount.getCpf())).thenReturn(destinationAccount);
        Mockito.when(transactionGateway.saveNewTransaction(clientTransaction)).thenReturn(clientTransaction);
        Mockito.doNothing().when(emailGateway).send(sourceAccount.getCpf());
        Mockito.doNothing().when(emailGateway).send(destinationAccount.getCpf());

        ClientTransaction newDeposit = createNewTransaction.execute(clientTransaction);

        Assertions.assertAll(
                () -> Assertions.assertEquals(42L, newDeposit.getId()),
                () -> Assertions.assertEquals(TRANSFERENCIA, newDeposit.getClientTransactionType()),
                () -> Assertions.assertEquals(BigDecimal.valueOf(250), newDeposit.getClientTransactionValue()),
                () -> Assertions.assertEquals("30941720081", newDeposit.getDestinationAccount().getCpf()),
                () -> Assertions.assertEquals("30941720080", newDeposit.getSourceAccount().getCpf())
        );
    }

    @Test
    public void shouldThrowExceptionWhenDepositIsNegative() throws Exception {
        Conta account =
                new Conta(4242L, 42L, Conta.AccountType.CORRENTE, BigDecimal.ZERO, "Bruno", "30941720080");

        ClientTransaction clientTransaction = new ClientTransaction(42L, DEPOSITO,
                account, account, BigDecimal.valueOf(-500));

        Throwable throwable = Assertions.assertThrows(Exception.class,
                () -> createNewTransaction.execute(clientTransaction));

        Assertions.assertEquals("Transaction value must be greater than 0.", throwable.getMessage());

        // Make sure the transactions is NEVER saved and email not sent
        Mockito.verify(transactionGateway, Mockito.never()).saveNewTransaction(Mockito.any());
        Mockito.verify(emailGateway, Mockito.never()).send(Mockito.any());
    }

    @Test
    public void shouldThrowExceptionWhenWithdrawnIsNegative() throws  Exception {
        Conta account =
                new Conta(4242L, 42L, Conta.AccountType.CORRENTE, BigDecimal.ZERO, "Bruno", "30941720080");

        ClientTransaction clientTransaction = new ClientTransaction(42L, SAQUE,
                account, account, BigDecimal.valueOf(-500));

        Throwable throwable = Assertions.assertThrows(Exception.class,
                () -> createNewTransaction.execute(clientTransaction));

        Assertions.assertEquals("Transaction value must be greater than 0.", throwable.getMessage());

        // Make sure the transactions is NEVER saved and email not sent
        Mockito.verify(transactionGateway, Mockito.never()).saveNewTransaction(Mockito.any());
        Mockito.verify(emailGateway, Mockito.never()).send(Mockito.any());
    }
}
