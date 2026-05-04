package br.com.unisales.locadora.repository;

import br.com.unisales.locadora.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {}

