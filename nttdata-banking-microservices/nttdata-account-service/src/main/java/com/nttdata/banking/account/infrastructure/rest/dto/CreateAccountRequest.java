package com.nttdata.banking.account.infrastructure.rest.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAccountRequest {

    @NotBlank(message = "El número de cuenta es requerido")
    @Pattern(regexp = "^[0-9]{6,20}$", message = "El número de cuenta debe contener entre 6 y 20 dígitos")
    private String accountNumber;

    @NotBlank(message = "El tipo de cuenta es requerido")
    @Pattern(regexp = "^(AHORRO|CORRIENTE)$", message = "El tipo de cuenta debe ser AHORRO o CORRIENTE")
    private String accountType;

    @NotNull(message = "El saldo inicial es requerido")
    @DecimalMin(value = "0.0", inclusive = true, message = "El saldo inicial debe ser >= 0")
    private BigDecimal initialBalance;

    @NotBlank(message = "El código de cliente es requerido")
    private String customerCode;
}
