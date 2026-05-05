package br.com.unisales.locadora.api.error;

import br.com.unisales.locadora.service.BusinessException;
import br.com.unisales.locadora.service.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ApiError> handleNotFound(NotFoundException ex, HttpServletRequest req) {
    return build(HttpStatus.NOT_FOUND, ex.getMessage(), req.getRequestURI(), null);
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ApiError> handleBusiness(BusinessException ex, HttpServletRequest req) {
    return build(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI(), null);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
    List<ApiError.FieldError> fields = ex.getBindingResult().getFieldErrors().stream()
        .map(this::toFieldError)
        .toList();
    return build(HttpStatus.BAD_REQUEST, "Validação falhou", req.getRequestURI(), fields);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleUnexpected(Exception ex, HttpServletRequest req) {
    // Evita vazar stack trace e detalhes internos para o cliente.
    return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno", req.getRequestURI(), null);
  }

  // VULNERABILIDADE #7: Exposição de stack trace
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ApiError> handleUnexpectedVulneravel(RuntimeException ex, HttpServletRequest req) {
    // VULNERABILIDADE: Expor stack trace completo
    StringWriter sw = new StringWriter();
    ex.printStackTrace(new PrintWriter(sw));
    String stackTrace = sw.toString();

    ApiError body = new ApiError(
        OffsetDateTime.now(),
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
        ex.getMessage(), // Mensagem da exceção
        req.getRequestURI(), // path
        null, // fieldErrors
        stackTrace // stackTrace
    );
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }

  private ApiError.FieldError toFieldError(FieldError fe) {
    return new ApiError.FieldError(fe.getField(), fe.getDefaultMessage());
  }

  private ResponseEntity<ApiError> build(HttpStatus status, String message, String path,
      List<ApiError.FieldError> fieldErrors) {
    ApiError body = new ApiError(
        OffsetDateTime.now(),
        status.value(),
        status.getReasonPhrase(),
        message,
        path,
        fieldErrors,
        null // stackTrace
    );
    return ResponseEntity.status(status).body(body);
  }
}
