package org.example.library.controller;

import org.example.library.model.Client;
import org.example.library.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientControllerTest {

    @MockitoBean
    private ClientService clientService;
    private Long count;

    @LocalServerPort
    private int port;
    private RestClient restClient;

    @BeforeEach
    void beforeEach() {
        restClient = RestClient.create("http://localhost:" + port);
        count = 0L;
    }

    @Test
    void testGetByIdOk() {
        Client client = getClient();

        doReturn(client).when(clientService).getById(anyLong());

        ResponseEntity<Client> actual = restClient.get()
                .uri("/clients/" + client.getId())
                .retrieve()
                .toEntity(Client.class);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        Client body = actual.getBody();
        assertNotNull(body);
        assertEquals(client.getId(), body.getId());
    }

    @Test
    void testOkGetAll() {
        Client client = getClient();
        Client client1 = getClient();

        List<Client> clients = new ArrayList<>();
        clients.add(client1);
        clients.add(client);

        doReturn(clients).when(clientService).getAll();

        assertEquals(clients.size(), restClient.get()
                .uri("/clients")
                .retrieve()
                .toEntity(List.class)
                .getBody()
                .size());
    }

    @Test
    void testDeleteByIdAllOk() {
        doNothing().when(clientService).deleteById(anyLong());

        ResponseEntity<Void> bodilessEntity = restClient.delete()
                .uri("/clients/" + 1L)
                .retrieve()
                .toBodilessEntity();

        assertEquals(HttpStatus.NO_CONTENT, bodilessEntity.getStatusCode());
    }

    private Client getClient() {
        count++;
        Client client = new Client();
        client.setId(count);
        client.setName("User" + count);

        return client;
    }
}
