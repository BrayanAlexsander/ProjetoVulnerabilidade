package br.com.unisales.locadora.api.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record LocacaoDevolucaoRequest(
    @NotNull LocalDate dataDevolucao
) {}

