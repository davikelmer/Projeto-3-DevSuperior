package com.davikelmer.Projeto3.service;

import com.davikelmer.Projeto3.dto.ClientDTO;
import com.davikelmer.Projeto3.entities.Client;
import com.davikelmer.Projeto3.repositories.ClientRepository;
import com.davikelmer.Projeto3.service.exceptions.DatabaseException;
import com.davikelmer.Projeto3.service.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {
    @Autowired
    private ClientRepository repository;

    @Transactional(readOnly = true)
    public ClientDTO findById(Long id) {
        Client client = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado.")
        );
        return new ClientDTO(client);
    }
    @Transactional(readOnly = true)
    public Page<ClientDTO> findAll(Pageable pageable) {
        Page<Client> result = repository.findAll(pageable);
        return result.map(client -> new ClientDTO(client));
    }
    @Transactional
    public ClientDTO insert(ClientDTO dto) {
        Client client = new Client();
        copyToEntity(dto, client);
        client = repository.save(client);

        return new ClientDTO(client);
    }

    @Transactional
    public ClientDTO update(ClientDTO dto, long id) {
        try {
            Client client = repository.getReferenceById(id);
            copyToEntity(dto, client);
            client = repository.save(client);
            return new ClientDTO(client);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try {
            repository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    public void copyToEntity(ClientDTO dto, Client client) {
       client.setName(dto.getName());
       client.setBirthDate(dto.getBirthDate());
       client.setCpf(dto.getCpf());
       client.setChildren(dto.getChildren());
       client.setIncome(dto.getIncome());
    }
}
