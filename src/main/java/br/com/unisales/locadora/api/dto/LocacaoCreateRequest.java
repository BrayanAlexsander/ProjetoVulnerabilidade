package br.com.unisales.locadora.api.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record LocacaoCreateRequest(
    @NotNull Long clienteId,
    @NotNull Long jogoId,
    @NotNull @FutureOrPresent LocalDate dataLocacao,
    @NotNull @FutureOrPresent LocalDate dataPrevistaDevolucao
) {}

