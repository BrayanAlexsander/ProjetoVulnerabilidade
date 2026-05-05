package br.com.unisales.locadora.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "jogos")
public class Jogo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 180)
  private String titulo;

  @Column(nullable = false, length = 80)
  private String plataforma;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal precoDiaria;

  @Column(nullable = false)
  private boolean ativo = true;

  @Column(nullable = false)
  private OffsetDateTime criadoEm = OffsetDateTime.now();

  public Long getId() {
    return id;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getPlataforma() {
    return plataforma;
  }

  public void setPlataforma(String plataforma) {
    this.plataforma = plataforma;
  }

  public BigDecimal getPrecoDiaria() {
    return precoDiaria;
  }

  public void setPrecoDiaria(BigDecimal precoDiaria) {
    this.precoDiaria = precoDiaria;
  }

  public boolean isAtivo() {
    return ativo;
  }

  public void setAtivo(boolean ativo) {
    this.ativo = ativo;
  }

  public OffsetDateTime getCriadoEm() {
    return criadoEm;
  }
}

