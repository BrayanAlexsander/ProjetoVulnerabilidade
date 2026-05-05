package br.com.unisales.locadora.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record JogoRequest(
    @NotBlank @Size(min = 1, max = 180) String titulo,
    @NotBlank @Size(min = 2, max = 80) String plataforma,
    @NotNull @DecimalMin(value = "0.01") BigDecimal precoDiaria,
    Boolean ativo
) {}

