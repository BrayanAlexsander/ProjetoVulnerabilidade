package br.com.unisales.locadora.service;

import br.com.unisales.locadora.api.dto.LocacaoCreateRequest;
import br.com.unisales.locadora.domain.Cliente;
import br.com.unisales.locadora.domain.Jogo;
import br.com.unisales.locadora.domain.Locacao;
import br.com.unisales.locadora.repository.LocacaoRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocacaoService {
  private final LocacaoRepository locacaoRepository;
  private final ClienteService clienteService;
  private final JogoServiceAdapter jogoServiceAdapter;

  public LocacaoService(
      LocacaoRepository locacaoRepository,
      ClienteService clienteService,
      JogoServiceAdapter jogoServiceAdapter
  ) {
    this.locacaoRepository = locacaoRepository;
    this.clienteService = clienteService;
    this.jogoServiceAdapter = jogoServiceAdapter;
  }

  @Transactional
  public Locacao criar(LocacaoCreateRequest req) {
    if (req.dataPrevistaDevolucao().isBefore(req.dataLocacao())) {
      throw new BusinessException("Data prevista de devolução deve ser >= data de locação");
    }
    Cliente cliente = clienteService.buscar(req.clienteId());
    Jogo jogo = jogoServiceAdapter.buscar(req.jogoId());

    Locacao l = new Locacao();
    l.setCliente(cliente);
    l.setJogo(jogo);
    l.setDataLocacao(req.dataLocacao());
    l.setDataPrevistaDevolucao(req.dataPrevistaDevolucao());
    return locacaoRepository.save(l);
  }

  @Transactional(readOnly = true)
  public List<Locacao> listar() {
    return locacaoRepository.findAll();
  }

  @Transactional
  public Locacao devolver(Long id, LocalDate dataDevolucao) {
    Locacao l = locacaoRepository.findById(id).orElseThrow(() -> new NotFoundException("Locação não encontrada"));
    if (l.getDataDevolucao() != null) {
      throw new BusinessException("Locação já devolvida");
    }
    if (dataDevolucao.isBefore(l.getDataLocacao())) {
      throw new BusinessException("Data de devolução não pode ser < data de locação");
    }
    l.setDataDevolucao(dataDevolucao);
    return locacaoRepository.save(l);
  }

  /**
   * Adaptador pequeno para evitar dependência circular e manter a API estável.
   */
  @Service
  public static class JogoServiceAdapter {
    private final br.com.unisales.locadora.repository.JogoRepository jogoRepository;

    public JogoServiceAdapter(br.com.unisales.locadora.repository.JogoRepository jogoRepository) {
      this.jogoRepository = jogoRepository;
    }

    @Transactional(readOnly = true)
    public Jogo buscar(Long id) {
      return jogoRepository.findById(id).orElseThrow(() -> new NotFoundException("Jogo não encontrado"));
    }
  }
}

