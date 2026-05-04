package br.com.unisales.locadora.api.error;

import java.time.OffsetDateTime;
import java.util.List;

public record ApiError(
    OffsetDateTime timestamp,
    int status,
    String error,
    String message,
    String path,
    List<FieldError> fieldErrors
) {
  public record FieldError(String field, String message) {}
}

