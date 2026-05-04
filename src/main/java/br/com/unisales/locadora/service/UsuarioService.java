package br.com.unisales.locadora.service;

import br.com.unisales.locadora.api.dto.UsuarioCreateRequest;
import br.com.unisales.locadora.domain.Usuario;
import br.com.unisales.locadora.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {
  private final UsuarioRepository usuarioRepository;
  private final PasswordHasher passwordHasher;

  public UsuarioService(UsuarioRepository usuarioRepository, PasswordHasher passwordHasher) {
    this.usuarioRepository = usuarioRepository;
    this.passwordHasher = passwordHasher;
  }

  @Transactional
  public Usuario cadastrar(UsuarioCreateRequest req) {
    if (usuarioRepository.existsByEmail(req.email())) {
      throw new BusinessException("E-mail já cadastrado");
    }
    Usuario u = new Usuario();
    u.setNome(req.nome());
    u.setEmail(req.email());
    u.setSenhaHash(passwordHasher.hash(req.senha()));
    return usuarioRepository.save(u);
  }

  @Transactional(readOnly = true)
  public Usuario autenticar(String email, String senha) {
    Usuario u = usuarioRepository.findByEmail(email)
        .orElseThrow(() -> new BusinessException("Credenciais inválidas"));
    if (!passwordHasher.matches(senha, u.getSenhaHash())) {
      throw new BusinessException("Credenciais inválidas");
    }
    return u;
  }
}

