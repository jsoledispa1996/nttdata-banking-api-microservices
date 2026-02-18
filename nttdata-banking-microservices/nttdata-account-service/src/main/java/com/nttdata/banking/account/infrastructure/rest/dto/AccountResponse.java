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
public class AccountResponse {

    private Long id;
    private String accountNumber;
    private String accountType;
    private BigDecimal initialBalance;
    private BigDecimal currentBalance;
    private Boolean active;
    private String customerCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

