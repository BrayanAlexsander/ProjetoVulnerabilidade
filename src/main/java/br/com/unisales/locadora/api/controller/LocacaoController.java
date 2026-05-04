package br.com.unisales.locadora.api.controller;

import br.com.unisales.locadora.api.dto.LocacaoCreateRequest;
import br.com.unisales.locadora.api.dto.LocacaoDevolucaoRequest;
import br.com.unisales.locadora.api.dto.LocacaoResponse;
import br.com.unisales.locadora.domain.Locacao;
import br.com.unisales.locadora.service.LocacaoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/locacoes")
public class LocacaoController {
  private final LocacaoService locacaoService;

  public LocacaoController(LocacaoService locacaoService) {
    this.locacaoService = locacaoService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public LocacaoResponse criar(@Valid @RequestBody LocacaoCreateRequest req) {
    return toResponse(locacaoService.criar(req));
  }

  @GetMapping
  public List<LocacaoResponse> listar() {
    return locacaoService.listar().stream().map(this::toResponse).toList();
  }

  @PutMapping("/{id}/devolucao")
  public LocacaoResponse devolver(@PathVariable Long id, @Valid @RequestBody LocacaoDevolucaoRequest req) {
    return toResponse(locacaoService.devolver(id, req.dataDevolucao()));
  }

  private LocacaoResponse toResponse(Locacao l) {
    return new LocacaoResponse(
        l.getId(),
        l.getCliente().getId(),
        l.getJogo().getId(),
        l.getDataLocacao(),
        l.getDataPrevistaDevolucao(),
        l.getDataDevolucao(),
        l.getCriadoEm()
    );
  }
}

