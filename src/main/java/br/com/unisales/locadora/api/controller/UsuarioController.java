package br.com.unisales.locadora.api.controller;

import br.com.unisales.locadora.api.dto.UsuarioCreateRequest;
import br.com.unisales.locadora.api.dto.UsuarioResponse;
import br.com.unisales.locadora.domain.Usuario;
import br.com.unisales.locadora.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
  private final UsuarioService usuarioService;

  public UsuarioController(UsuarioService usuarioService) {
    this.usuarioService = usuarioService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UsuarioResponse cadastrar(@Valid @RequestBody UsuarioCreateRequest req) {
    Usuario u = usuarioService.cadastrar(req);
    return new UsuarioResponse(u.getId(), u.getNome(), u.getEmail(), u.getCriadoEm());
  }
}

