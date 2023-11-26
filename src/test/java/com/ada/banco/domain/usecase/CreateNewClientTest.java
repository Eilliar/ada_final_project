package com.ada.banco.domain.usecase;

import com.ada.banco.domain.gateway.ClientGateway;
import com.ada.banco.domain.gateway.EmailGateway;
import com.ada.banco.domain.model.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreateNewClientTest {
    @Mock
    ClientGateway clientGateway;

    @Mock
    EmailGateway emailGateway;

    @InjectMocks
    CreateNewClient createNewClient;

    @Test
    public void shouldCreateNewClient() throws Exception{
        Client client = new Client(42L, "Bruno", "Godoi Eilliar", "30941720080");

        // Configure Mocks to behave in a certain way
        Mockito.when(clientGateway.searchByCpf(client.getCpf())).thenReturn(null);
        Mockito.when(clientGateway.saveNewClient(client)).thenReturn(client);
        Mockito.doNothing().when(emailGateway).send(client.getCpf());

        Client newClient = createNewClient.execute(client);

        Assertions.assertAll(
                () -> Assertions.assertEquals(42L, newClient.getId()),
                () -> Assertions.assertEquals("Bruno", newClient.getFirstName()),
                () -> Assertions.assertEquals("Godoi Eilliar", newClient.getLastName()),
                () -> Assertions.assertEquals("30941720080", newClient.getCpf())
        );
    }

    @Test
    public void shouldThrowExceptionWhenClientAlreadyExists(){
        Client client = new Client("Bruno", "Godoi Eilliar", "30941720080");

        // We have to force the gateway to return the above account when searching for it
        Mockito.when(clientGateway.searchByCpf(client.getCpf())).thenReturn(client);

        Throwable throwable = Assertions.assertThrows(Exception.class, ()->createNewClient.execute(client));

        Assertions.assertEquals("User already exists.", throwable.getMessage());

    }

}
