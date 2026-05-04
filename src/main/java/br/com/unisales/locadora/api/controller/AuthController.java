package br.com.unisales.locadora.api.controller;

import br.com.unisales.locadora.api.dto.LoginRequest;
import br.com.unisales.locadora.api.dto.LoginResponse;
import br.com.unisales.locadora.domain.Usuario;
import br.com.unisales.locadora.service.UsuarioService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
  private final UsuarioService usuarioService;

  public AuthController(UsuarioService usuarioService) {
    this.usuarioService = usuarioService;
  }

  @PostMapping("/login")
  public LoginResponse login(@Valid @RequestBody LoginRequest req) {
    Usuario u = usuarioService.autenticar(req.email(), req.senha());
    String token = "dummy-" + UUID.randomUUID();
    return new LoginResponse(u.getId(), u.getEmail(), token);
  }
}

