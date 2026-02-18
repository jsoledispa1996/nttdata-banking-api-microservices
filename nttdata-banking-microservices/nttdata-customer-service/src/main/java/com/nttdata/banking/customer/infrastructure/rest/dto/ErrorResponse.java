package com.nttdata.banking.customer.infrastructure.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Standard error response DTO.
 * Returned when an exception occurs during API processing.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private String timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
    private String correlationId;

    public static ErrorResponse of(Integer status, String error, String message, String path, String correlationId) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .correlationId(correlationId)
                .build();
    }
}
