package br.com.unisales.locadora.repository;

import br.com.unisales.locadora.domain.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
  Optional<Usuario> findByEmail(String email);
  boolean existsByEmail(String email);
}

