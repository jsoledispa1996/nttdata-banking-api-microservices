package com.nttdata.banking.account.infrastructure.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountStatementResponse {

    private String customerCode;
    private LocalDateTime reportDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<AccountStatementItem> accounts;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AccountStatementItem {
        private String accountNumber;
        private String accountType;
        private BigDecimal initialBalance;
        private BigDecimal currentBalance;
        private Boolean active;
        private List<MovementItem> movements;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MovementItem {
        private LocalDateTime date;
        private String type;
        private BigDecimal amount;
        private BigDecimal balance;
        private String description;
    }
}
