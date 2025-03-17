package org.example.library.service;

import org.example.library.exception.NotFoundException;
import org.example.library.model.Client;
import org.example.library.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client getById(Long id) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        return optionalClient.orElseThrow(NotFoundException::new);
    }

    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    public Client create(Client client) {
        return clientRepository.save(client);
    }

    public void deleteById(Long id) {
        clientRepository.deleteById(id);
    }
}
