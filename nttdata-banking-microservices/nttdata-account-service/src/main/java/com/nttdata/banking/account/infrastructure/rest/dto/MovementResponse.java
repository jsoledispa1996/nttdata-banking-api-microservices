package com.nttdata.banking.account.infrastructure.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovementResponse {

    private Long id;
    private LocalDateTime movementDate;
    private String movementType;
    private BigDecimal amount;
    private BigDecimal balance;
    private String description;
    private Long accountId;
    private String accountNumber;
    private LocalDateTime createdAt;
}
