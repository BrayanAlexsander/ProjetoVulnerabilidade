package br.com.unisales.locadora.api.dto;

public record LoginResponse(
    Long usuarioId,
    String email,
    String token
) {}

