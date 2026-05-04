package br.com.unisales.locadora.repository;

import br.com.unisales.locadora.domain.Locacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocacaoRepository extends JpaRepository<Locacao, Long> {}

