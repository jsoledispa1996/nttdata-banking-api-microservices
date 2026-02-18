package com.nttdata.banking.customer.infrastructure.rest.exception;

import com.nttdata.banking.customer.infrastructure.rest.dto.ErrorResponse;
import com.nttdata.banking.shared.exceptions.BusinessException;
import com.nttdata.banking.shared.exceptions.ResourceNotFoundException;
import com.nttdata.banking.shared.utils.CorrelationIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle ResourceNotFoundException (404 Not Found).
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, ServerWebExchange exchange) {
        String correlationId = CorrelationIdUtil.getCorrelationId();
        String path = exchange.getRequest().getPath().value();

        log.warn("[{}] Recurso no encontrado: {}", correlationId, ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.NOT_FOUND.value(),
                "Recurso no encontrado",
                ex.getMessage(),
                path,
                correlationId
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Handle BusinessException (400 Bad Request or 409 Conflict).
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex, ServerWebExchange exchange) {
        String correlationId = CorrelationIdUtil.getCorrelationId();
        String path = exchange.getRequest().getPath().value();

        HttpStatus status = "DUPLICATE_IDENTIFICATION".equals(ex.getErrorCode())
                ? HttpStatus.CONFLICT
                : HttpStatus.BAD_REQUEST;

        log.warn("[{}] Excepción de negocio: {} - {}", correlationId, ex.getErrorCode(), ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
                status.value(),
                ex.getErrorCode(),
                ex.getMessage(),
                path,
                correlationId
        );

        return ResponseEntity.status(status).body(errorResponse);
    }

    /**
     * Handle validation errors (400 Bad Request).
     * Triggered when @Valid fails on request body.
     */
    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            WebExchangeBindException ex, ServerWebExchange exchange) {
        String correlationId = CorrelationIdUtil.getCorrelationId();
        String path = exchange.getRequest().getPath().value();

        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.warn("[{}] Validación fallida: {}", correlationId, errorMessage);

        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                "Validación fallida",
                errorMessage,
                path,
                correlationId
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle generic exceptions (500 Internal Server Error).
     * Catch-all for unexpected errors.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, ServerWebExchange exchange) {
        String correlationId = CorrelationIdUtil.getCorrelationId();
        String path = exchange.getRequest().getPath().value();

        log.error("[{}] Error inesperado ocurrido", correlationId, ex);

        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error interno del servidor",
                "Ha ocurrido un error inesperado. Por favor, intente nuevamente más tarde.",
                path,
                correlationId
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
