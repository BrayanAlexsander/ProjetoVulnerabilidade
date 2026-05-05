package br.com.unisales.locadora.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
    @NotBlank @Size(min = 2, max = 140) String nome,
    @NotBlank @Size(min = 5, max = 40) String documento
) {}

