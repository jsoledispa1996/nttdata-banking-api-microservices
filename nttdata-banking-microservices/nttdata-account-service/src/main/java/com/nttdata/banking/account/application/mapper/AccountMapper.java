package com.nttdata.banking.account.application.mapper;

import com.nttdata.banking.account.domain.model.Account;
import com.nttdata.banking.account.domain.model.AccountType;
import com.nttdata.banking.account.infrastructure.rest.dto.AccountResponse;
import com.nttdata.banking.account.infrastructure.rest.dto.CreateAccountRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AccountMapper {

    /**
     * Convert CreateAccountRequest to Account entity.
     *
     * @param request Create account request
     * @return Account entity
     */
    public Account toEntity(CreateAccountRequest request) {
        return Account.builder()
                .accountNumber(request.getAccountNumber())
                .accountType(AccountType.valueOf(request.getAccountType()))
                .initialBalance(request.getInitialBalance())
                .currentBalance(request.getInitialBalance()) // Initially same as initial balance
                .active(true)
                .customerId(request.getCustomerCode())
                .createdDate(LocalDateTime.now())
                .build();
    }

    /**
     * Convert Account entity to AccountResponse DTO.
     *
     * @param account Account entity
     * @return AccountResponse DTO
     */
    public AccountResponse toResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountType(String.valueOf(account.getAccountType()))
                .initialBalance(account.getInitialBalance())
                .currentBalance(account.getCurrentBalance())
                .active(account.getActive())
                .customerCode(account.getCustomerId())
                .createdAt(account.getCreatedDate())
                .updatedAt(account.getUpdatedDate())
                .build();
    }
}
