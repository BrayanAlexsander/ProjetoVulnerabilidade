package br.com.unisales.locadora.api.controller;

import br.com.unisales.locadora.api.dto.ClienteRequest;
import br.com.unisales.locadora.api.dto.ClienteResponse;
import br.com.unisales.locadora.domain.Cliente;
import br.com.unisales.locadora.service.ClienteService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
  private final ClienteService clienteService;

  public ClienteController(ClienteService clienteService) {
    this.clienteService = clienteService;
  }

  @GetMapping
  public List<ClienteResponse> listar() {
    return clienteService.listar().stream().map(this::toResponse).toList();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ClienteResponse criar(@Valid @RequestBody ClienteRequest req) {
    Cliente c = clienteService.criar(req);
    return toResponse(c);
  }

  // VULNERABILIDADE #9: IDOR - acesso sem validação
  @GetMapping("/{id}/dados-completos")
  public ClienteDados obterDadosCompletos(@PathVariable Long id) {
    // VULNERABILIDADE: Sem validação de permissão
    Cliente c = clienteService.buscar(id);
    return new ClienteDados(
        c.getId(),
        c.getNome(),
        c.getDocumento(),
        c.getNumeroCartao() // Dado sensível
    );
  }

  private ClienteResponse toResponse(Cliente c) {
    return new ClienteResponse(c.getId(), c.getNome(), c.getDocumento(), c.getCriadoEm());
  }
}

// VULNERABILIDADE #9: Record para dados sensíveis
record ClienteDados(Long id, String nome, String documento, String numeroCartao) {
}
