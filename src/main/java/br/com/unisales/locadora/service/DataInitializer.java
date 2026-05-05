package br.com.unisales.locadora.service;

import br.com.unisales.locadora.api.dto.ClienteRequest;
import br.com.unisales.locadora.api.dto.JogoRequest;
import br.com.unisales.locadora.api.dto.LocacaoCreateRequest;
import br.com.unisales.locadora.api.dto.UsuarioCreateRequest;
import br.com.unisales.locadora.domain.Cliente;
import br.com.unisales.locadora.domain.Jogo;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
  private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
  private final UsuarioService usuarioService;
  private final ClienteService clienteService;
  private final JogoService jogoService;
  private final LocacaoService locacaoService;

  public DataInitializer(
      UsuarioService usuarioService,
      ClienteService clienteService,
      JogoService jogoService,
      LocacaoService locacaoService
  ) {
    this.usuarioService = usuarioService;
    this.clienteService = clienteService;
    this.jogoService = jogoService;
    this.locacaoService = locacaoService;
  }

  @Override
  public void run(String... args) {
    createUsuario("Administrador", "admin@locadora.com", "Admin1234");
    createUsuario("Usuário Padrão", "usuario@locadora.com", "Senha1234");

    ensureClientes();
    ensureJogos();
    ensureLocacoes();
  }

  private void createUsuario(String nome, String email, String senha) {
    try {
      usuarioService.cadastrar(new UsuarioCreateRequest(nome, email, senha));
      log.info("Usuário inicializado: {}", email);
    } catch (BusinessException ex) {
      log.info("Usuário já existe: {}", email);
    }
  }

  private void ensureClientes() {
    List<Cliente> clientes = clienteService.listar();
    if (clientes.size() >= 3) {
      return;
    }

    createCliente("Maria Silva", "123.456.789-00");
    createCliente("Carlos Santos", "987.654.321-00");
    createCliente("Julia Costa", "111.222.333-44");
  }

  private void createCliente(String nome, String documento) {
    try {
      clienteService.criar(new ClienteRequest(nome, documento));
      log.info("Cliente inicializado: {}", nome);
    } catch (BusinessException ex) {
      log.info("Cliente já existe: {}", nome);
    }
  }

  private void ensureJogos() {
    List<Jogo> jogos = jogoService.listar();
    if (jogos.size() >= 3) {
      return;
    }

    createJogo("The Last of Us", "PlayStation", new BigDecimal("14.90"));
    createJogo("FIFA 24", "Xbox", new BigDecimal("9.90"));
    createJogo("Super Mario Odyssey", "Nintendo Switch", new BigDecimal("12.50"));
  }

  private void createJogo(String titulo, String plataforma, BigDecimal precoDiaria) {
    try {
      jogoService.criar(new JogoRequest(titulo, plataforma, precoDiaria, true));
      log.info("Jogo inicializado: {}", titulo);
    } catch (BusinessException ex) {
      log.info("Jogo já existe: {}", titulo);
    }
  }

  private void ensureLocacoes() {
    if (locacaoService.listar().size() >= 3) {
      return;
    }

    List<Cliente> clientes = clienteService.listar();
    List<Jogo> jogos = jogoService.listar();
    if (clientes.size() < 3 || jogos.size() < 3) {
      log.warn("Não há clientes ou jogos suficientes para criar locações de exemplo");
      return;
    }

    LocalDate hoje = LocalDate.now();

    createLocacao(clientes.get(0).getId(), jogos.get(0).getId(), hoje, hoje.plusDays(5));
    createLocacao(clientes.get(1).getId(), jogos.get(1).getId(), hoje, hoje.plusDays(7));
    createLocacao(clientes.get(2).getId(), jogos.get(2).getId(), hoje, hoje.plusDays(10));
  }

  private void createLocacao(Long clienteId, Long jogoId, LocalDate dataLocacao, LocalDate dataPrevista) {
    try {
      locacaoService.criar(new LocacaoCreateRequest(clienteId, jogoId, dataLocacao, dataPrevista));
      log.info("Locação inicializada: clienteId={}, jogoId={}", clienteId, jogoId);
    } catch (BusinessException ex) {
      log.info("Locação não criada: clienteId={}, jogoId={} - {}", clienteId, jogoId, ex.getMessage());
    }
  }
}
