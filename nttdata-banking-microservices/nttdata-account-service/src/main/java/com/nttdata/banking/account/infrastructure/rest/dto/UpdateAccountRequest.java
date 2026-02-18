package com.nttdata.banking.account.infrastructure.rest.dto;

import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAccountRequest {

    @DecimalMin(value = "0.0", message = "El saldo debe ser >= 0")
    private BigDecimal currentBalance;

    private Boolean active;
}
