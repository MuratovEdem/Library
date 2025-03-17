package org.example.library.service;

import org.example.library.exception.NotFoundException;
import org.example.library.model.Client;
import org.example.library.repository.ClientRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ClientServiceTest {

    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientRepository clientRepository;

    private Long count;

    private final static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.8-alpine3.18");

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
    }

    @BeforeEach
    void BeforeEach() {
        clientRepository.deleteAll();
        count = 0L;
    }

    @Test
    void testGetByIdOk() {
        Client client = getClient();

        clientRepository.save(client);

        Client actual = clientService.getById(client.getId());

        assertEquals(client.getId(), actual.getId());
        assertEquals(client.getName(), actual.getName());
    }

    @Test
    void testGetByIdExpectedException() {
        assertThrows(NotFoundException.class, () -> clientService.getById(1L));
    }

    @Test
    void testGetAllOk() {
        Client client = getClient();
        Client client1 = getClient();

        clientRepository.save(client);
        clientRepository.save(client1);

        List<Client> clientList = clientService.getAll();

        assertEquals(clientList.size(), 2);
    }

    @Test
    void testDeleteByIdAllOk() {
        Client client = getClient();

        Client actual = clientRepository.save(client);

        clientService.deleteById(actual.getId());

        assertTrue(clientRepository.findById(actual.getId()).isEmpty());
    }

    private Client getClient() {
        count++;
        Client client = new Client();
        client.setId(count);
        client.setName("User" + count);

        return client;
    }
}
