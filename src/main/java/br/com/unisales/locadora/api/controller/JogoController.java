package br.com.unisales.locadora.api.controller;

import br.com.unisales.locadora.api.dto.JogoRequest;
import br.com.unisales.locadora.api.dto.JogoResponse;
import br.com.unisales.locadora.domain.Jogo;
import br.com.unisales.locadora.service.JogoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jogos")
public class JogoController {
  private final JogoService jogoService;

  public JogoController(JogoService jogoService) {
    this.jogoService = jogoService;
  }

  @GetMapping
  public List<JogoResponse> listar() {
    return jogoService.listar().stream().map(this::toResponse).toList();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public JogoResponse criar(@Valid @RequestBody JogoRequest req) {
    return toResponse(jogoService.criar(req));
  }

  @PutMapping("/{id}")
  public JogoResponse atualizar(@PathVariable Long id, @Valid @RequestBody JogoRequest req) {
    return toResponse(jogoService.atualizar(id, req));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deletar(@PathVariable Long id) {
    jogoService.deletar(id);
  }

  private JogoResponse toResponse(Jogo j) {
    return new JogoResponse(j.getId(), j.getTitulo(), j.getPlataforma(), j.getPrecoDiaria(), j.isAtivo(), j.getCriadoEm());
  }
}

