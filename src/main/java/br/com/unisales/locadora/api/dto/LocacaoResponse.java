package br.com.unisales.locadora.api.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record LocacaoResponse(
    Long id,
    Long clienteId,
    Long jogoId,
    LocalDate dataLocacao,
    LocalDate dataPrevistaDevolucao,
    LocalDate dataDevolucao,
    OffsetDateTime criadoEm
) {}

