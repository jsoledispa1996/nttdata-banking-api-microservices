package com.nttdata.banking.account.application.mapper;

import com.nttdata.banking.account.domain.model.Account;
import com.nttdata.banking.account.domain.model.Movement;
import com.nttdata.banking.account.infrastructure.rest.dto.MovementResponse;
import org.springframework.stereotype.Component;


@Component
public class MovementMapper {

    /**
     * Convert Movement entity to MovementResponse DTO.
     *
     * @param movement Movement entity
     * @param account Account entity (for account number)
     * @return MovementResponse DTO
     */
    public MovementResponse toResponse(Movement movement, Account account) {
        return MovementResponse.builder()
                .id(movement.getId())
                .movementDate(movement.getMovementDate())
                .movementType(movement.getMovementType().name())
                .amount(movement.getAmount())
                .balance(movement.getBalance())
                .description(movement.getDescription())
                .accountId(movement.getAccountId())
                .accountNumber(account.getAccountNumber())
                .build();
    }

    /**
     * Convert Movement entity to MovementResponse DTO (without account).
     *
     * @param movement Movement entity
     * @return MovementResponse DTO
     */
    public MovementResponse toResponse(Movement movement) {
        return MovementResponse.builder()
                .id(movement.getId())
                .movementDate(movement.getMovementDate())
                .movementType(movement.getMovementType().name())
                .amount(movement.getAmount())
                .balance(movement.getBalance())
                .description(movement.getDescription())
                .accountId(movement.getAccountId())
                .build();
    }
}
