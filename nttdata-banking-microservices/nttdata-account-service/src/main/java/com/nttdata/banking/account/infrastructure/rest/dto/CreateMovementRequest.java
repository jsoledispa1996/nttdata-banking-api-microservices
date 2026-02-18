package com.nttdata.banking.account.infrastructure.rest.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * F3: Service layer will validate sufficient balance for RETIRO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMovementRequest {

    @NotBlank(message = "El número de cuenta es requerido")
    private String accountNumber;

    @NotBlank(message = "El tipo de movimiento es requerido")
    @Pattern(regexp = "^(DEPOSITO|RETIRO)$", message = "El tipo de movimiento debe ser DEPOSITO o RETIRO")
    private String movementType;

    @NotNull(message = "El monto es requerido")
    @DecimalMin(value = "0.01", inclusive = true, message = "El monto debe ser mayor que 0")
    private BigDecimal amount;

    @Size(max = 255, message = "La descripción no debe exceder 255 caracteres")
    private String description;
}
