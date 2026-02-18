package com.nttdata.banking.account.infrastructure.rest.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private String timestamp;
    
    @JsonProperty("estado")
    private Integer status;
    
    private String error;
    
    @JsonProperty("mensaje")
    private String message;
    
    @JsonProperty("ruta")
    private String path;
    
    @JsonProperty("idCorrelacion")
    private String correlationId;
    
    @JsonProperty("detalles")
    private Map<String, String> details;

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
