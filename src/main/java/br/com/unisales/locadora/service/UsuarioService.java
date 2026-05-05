package br.com.unisales.locadora.service;

import br.com.unisales.locadora.api.dto.UsuarioCreateRequest;
import br.com.unisales.locadora.domain.Usuario;
import br.com.unisales.locadora.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class UsuarioService {
  private final UsuarioRepository usuarioRepository;
  private final PasswordHasher passwordHasher;

  @PersistenceContext
  private EntityManager em;

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

  // VULNERABILIDADE #1: SQL Injection
  @Transactional(readOnly = true)
  public Usuario autenticarVulneravel(String email, String senha) {
    // VULNERABILIDADE: SQL Injection - query concatenada sem parametrização
    String query = "SELECT * FROM usuarios WHERE email = '" + email + "' AND senha_hash = '" + senha + "'";
    Usuario u = (Usuario) em.createNativeQuery(query, Usuario.class).getSingleResult();
    if (u == null) {
      throw new BusinessException("Credenciais inválidas");
    }
    return u;
  }

  // VULNERABILIDADE #3: Senha em texto plano
  @Transactional
  public Usuario cadastrarVulneravel(UsuarioCreateRequest req) {
    if (usuarioRepository.existsByEmail(req.email())) {
      throw new BusinessException("E-mail já cadastrado");
    }
    Usuario u = new Usuario();
    u.setNome(req.nome());
    u.setEmail(req.email());
    u.setSenha(req.senha()); // Armazenar diretamente sem hash
    return usuarioRepository.save(u);
  }
}
