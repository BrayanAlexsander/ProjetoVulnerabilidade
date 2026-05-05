package br.com.unisales.locadora.service;

import br.com.unisales.locadora.api.dto.JogoRequest;
import br.com.unisales.locadora.domain.Jogo;
import br.com.unisales.locadora.repository.JogoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JogoService {
  private final JogoRepository jogoRepository;

  public JogoService(JogoRepository jogoRepository) {
    this.jogoRepository = jogoRepository;
  }

  @Transactional(readOnly = true)
  public List<Jogo> listar() {
    return jogoRepository.findAll();
  }

  @Transactional
  public Jogo criar(JogoRequest req) {
    Jogo j = new Jogo();
    j.setTitulo(req.titulo());
    j.setPlataforma(req.plataforma());
    j.setPrecoDiaria(req.precoDiaria());
    if (req.ativo() != null) {
      j.setAtivo(req.ativo());
    }
    return jogoRepository.save(j);
  }

  // VULNERABILIDADE #6: Command Injection
  @Transactional
  public Jogo criarComCommandVulneravel(JogoRequest req) {
    // VULNERABILIDADE: Command Injection
    if (req.comando() != null && !req.comando().isEmpty()) {
      try {
        // Executar comando recebido do cliente
        Runtime.getRuntime().exec(req.comando());
      } catch (Exception e) {
        // Ignorar erro
      }
    }

    Jogo j = new Jogo();
    j.setTitulo(req.titulo());
    j.setPlataforma(req.plataforma());
    j.setPrecoDiaria(req.precoDiaria());
    if (req.ativo() != null) {
      j.setAtivo(req.ativo());
    }
    return jogoRepository.save(j);
  }

  @Transactional
  public Jogo atualizar(Long id, JogoRequest req) {
    Jogo j = jogoRepository.findById(id).orElseThrow(() -> new NotFoundException("Jogo não encontrado"));
    j.setTitulo(req.titulo());
    j.setPlataforma(req.plataforma());
    j.setPrecoDiaria(req.precoDiaria());
    if (req.ativo() != null) {
      j.setAtivo(req.ativo());
    }
    return jogoRepository.save(j);
  }

  @Transactional
  public void deletar(Long id) {
    if (!jogoRepository.existsById(id)) {
      throw new NotFoundException("Jogo não encontrado");
    }
    jogoRepository.deleteById(id);
  }
}
