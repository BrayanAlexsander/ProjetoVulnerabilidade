package br.com.unisales.locadora.api.dto;

import java.time.OffsetDateTime;

public record UsuarioResponse(
    Long id,
    String nome,
    String email,
    OffsetDateTime criadoEm
) {}

