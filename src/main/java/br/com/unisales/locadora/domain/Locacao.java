package br.com.unisales.locadora.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "locacoes")
public class Locacao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "cliente_id", nullable = false, foreignKey = @ForeignKey(name = "fk_locacoes_cliente"))
  private Cliente cliente;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "jogo_id", nullable = false, foreignKey = @ForeignKey(name = "fk_locacoes_jogo"))
  private Jogo jogo;

  @Column(nullable = false)
  private LocalDate dataLocacao;

  @Column(nullable = false)
  private LocalDate dataPrevistaDevolucao;

  @Column
  private LocalDate dataDevolucao;

  @Column(nullable = false)
  private OffsetDateTime criadoEm = OffsetDateTime.now();

  public Long getId() {
    return id;
  }

  public Cliente getCliente() {
    return cliente;
  }

  public void setCliente(Cliente cliente) {
    this.cliente = cliente;
  }

  public Jogo getJogo() {
    return jogo;
  }

  public void setJogo(Jogo jogo) {
    this.jogo = jogo;
  }

  public LocalDate getDataLocacao() {
    return dataLocacao;
  }

  public void setDataLocacao(LocalDate dataLocacao) {
    this.dataLocacao = dataLocacao;
  }

  public LocalDate getDataPrevistaDevolucao() {
    return dataPrevistaDevolucao;
  }

  public void setDataPrevistaDevolucao(LocalDate dataPrevistaDevolucao) {
    this.dataPrevistaDevolucao = dataPrevistaDevolucao;
  }

  public LocalDate getDataDevolucao() {
    return dataDevolucao;
  }

  public void setDataDevolucao(LocalDate dataDevolucao) {
    this.dataDevolucao = dataDevolucao;
  }

  public OffsetDateTime getCriadoEm() {
    return criadoEm;
  }
}

