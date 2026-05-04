package br.com.unisales.locadora.api.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record JogoResponse(
    Long id,
    String titulo,
    String plataforma,
    BigDecimal precoDiaria,
    boolean ativo,
    OffsetDateTime criadoEm
) {}

