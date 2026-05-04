package br.com.unisales.locadora.api.dto;

import java.time.OffsetDateTime;

public record ClienteResponse(
    Long id,
    String nome,
    String documento,
    OffsetDateTime criadoEm
) {}

