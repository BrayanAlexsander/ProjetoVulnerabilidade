package br.com.unisales.locadora.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import org.springframework.stereotype.Component;

/**
 * Implementação simples para manter o projeto autocontido.
 * Em produção, prefira BCrypt/Argon2 com salt e custo configurável.
 */
@Component
public class PasswordHasher {
  public String hash(String raw) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] digest = md.digest(raw.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(digest);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("Algoritmo de hash indisponível", e);
    }
  }

  public boolean matches(String raw, String hashed) {
    return hash(raw).equals(hashed);
  }
}

