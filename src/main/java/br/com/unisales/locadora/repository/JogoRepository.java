package br.com.unisales.locadora.repository;

import br.com.unisales.locadora.domain.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JogoRepository extends JpaRepository<Jogo, Long> {}

