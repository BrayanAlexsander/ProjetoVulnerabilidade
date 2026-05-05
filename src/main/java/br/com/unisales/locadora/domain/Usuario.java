package br.com.unisales.locadora.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "usuarios", uniqueConstraints = {
    @UniqueConstraint(name = "uk_usuarios_email", columnNames = {"email"})
})
public class Usuario {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 120)
  private String nome;

  @Column(nullable = false, length = 180)
  private String email;

  @Column(nullable = false, length = 120)
  private String senhaHash;

  @Column(nullable = false)
  private OffsetDateTime criadoEm = OffsetDateTime.now();

  public Long getId() {
    return id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getSenhaHash() {
    return senhaHash;
  }

  public void setSenhaHash(String senhaHash) {
    this.senhaHash = senhaHash;
  }

  public OffsetDateTime getCriadoEm() {
    return criadoEm;
  }
}

