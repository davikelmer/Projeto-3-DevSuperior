package com.davikelmer.Projeto3.repositories;

import com.davikelmer.Projeto3.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
