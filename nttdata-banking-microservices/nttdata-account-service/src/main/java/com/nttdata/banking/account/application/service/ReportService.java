package com.nttdata.banking.account.application.service;


import com.nttdata.banking.account.domain.model.Account;
import com.nttdata.banking.account.domain.model.Movement;
import com.nttdata.banking.account.domain.repository.AccountRepository;
import com.nttdata.banking.account.domain.repository.MovementRepository;
import com.nttdata.banking.account.infrastructure.rest.dto.AccountStatementResponse;
import com.nttdata.banking.shared.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ReportService {

    private final AccountRepository accountRepository;
    private final MovementRepository movementRepository;

    /**
     * Constructor injection for all dependencies.
     */
    public ReportService(
            AccountRepository accountRepository,
            MovementRepository movementRepository) {
        this.accountRepository = accountRepository;
        this.movementRepository = movementRepository;
    }

    /**
     * Generate account statement report for a customer (F4).
     */
    public Mono<AccountStatementResponse> generateAccountStatement(
            String customerCode, LocalDateTime startDate, LocalDateTime endDate) {

        log.info("Generando estado de cuenta para cliente: {} desde {} hasta {}",
                customerCode, startDate, endDate);

        // Validate date range
        if (startDate.isAfter(endDate)) {
            return Mono.error(new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de fin"));
        }

        // Find all accounts for the customer
        return accountRepository.findByCustomerId(customerCode)
                .collectList()
                .flatMap(accounts -> {
                    if (accounts.isEmpty()) {
                        return Mono.error(new ResourceNotFoundException(
                                "No se encontraron cuentas para el cliente", customerCode));
                    }

                    // Build report for each account
                    List<Mono<AccountStatementResponse.AccountStatementItem>> accountItems = new ArrayList<>();

                    for (Account account : accounts) {
                        Mono<AccountStatementResponse.AccountStatementItem> accountItem =
                                buildAccountItem(account, startDate, endDate);
                        accountItems.add(accountItem);
                    }

                    // Combine all account items into a single report
                    return Mono.zip(accountItems, results -> {
                        List<AccountStatementResponse.AccountStatementItem> items = new ArrayList<>();
                        for (Object result : results) {
                            items.add((AccountStatementResponse.AccountStatementItem) result);
                        }

                        return AccountStatementResponse.builder()
                                .customerCode(customerCode)
                                .reportDate(LocalDateTime.now())
                                .startDate(startDate)
                                .endDate(endDate)
                                .accounts(items)
                                .build();
                    });
                })
                .doOnSuccess(report -> log.info(
                        "Account statement generated for customer: {} with {} accounts",
                        customerCode, report.getAccounts().size()));
    }

    /**
     * Generate complete customer report with all accounts and all movements (without date filter).
     */
    public Mono<AccountStatementResponse> getCustomerFullReport(String customerCode) {
        log.info("Generando reporte completo para cliente: {}", customerCode);

        // Find all accounts for the customer
        return accountRepository.findByCustomerId(customerCode)
                .collectList()
                .flatMap(accounts -> {
                    if (accounts.isEmpty()) {
                        return Mono.error(new ResourceNotFoundException(
                                "No se encontraron cuentas para el cliente", customerCode));
                    }

                    // Build report for each account with ALL movements
                    List<Mono<AccountStatementResponse.AccountStatementItem>> accountItems = new ArrayList<>();

                    for (Account account : accounts) {
                        Mono<AccountStatementResponse.AccountStatementItem> accountItem =
                                buildAccountItemWithAllMovements(account);
                        accountItems.add(accountItem);
                    }

                    // Combine all account items into a single report
                    return Mono.zip(accountItems, results -> {
                        List<AccountStatementResponse.AccountStatementItem> items = new ArrayList<>();
                        for (Object result : results) {
                            items.add((AccountStatementResponse.AccountStatementItem) result);
                        }

                        return AccountStatementResponse.builder()
                                .customerCode(customerCode)
                                .reportDate(LocalDateTime.now())
                                .startDate(null) // No date filter
                                .endDate(null)   // No date filter
                                .accounts(items)
                                .build();
                    });
                })
                .doOnSuccess(report -> {
                    int totalMovements = report.getAccounts().stream()
                            .mapToInt(acc -> acc.getMovements().size())
                            .sum();
                    log.info("Reporte completo generado para cliente: {} con {} cuentas y {} movimientos totales",
                            customerCode, report.getAccounts().size(), totalMovements);
                });
    }

    /**
     * Build account statement item with movements.
     *
     * @param account Account entity
     * @param startDate Start date for movements
     * @param endDate End date for movements
     * @return Mono of AccountStatementItem
     */
    private Mono<AccountStatementResponse.AccountStatementItem> buildAccountItem(
            Account account, LocalDateTime startDate, LocalDateTime endDate) {

        // Get movements for the account within date range
        return movementRepository.findByAccountIdAndMovementDateBetweenOrderByMovementDateDesc(
                        account.getId(), startDate, endDate)
                .map(this::mapMovementToItem)
                .collectList()
                .map(movements -> AccountStatementResponse.AccountStatementItem.builder()
                        .accountNumber(account.getAccountNumber())
                        .accountType(account.getAccountType().name())
                        .initialBalance(account.getInitialBalance())
                        .currentBalance(account.getCurrentBalance())
                        .active(account.getActive())
                        .movements(movements)
                        .build());
    }

    /**
     * Build account statement item with ALL movements (no date filter).
     *
     * @param account Account entity
     * @return Mono of AccountStatementItem
     */
    private Mono<AccountStatementResponse.AccountStatementItem> buildAccountItemWithAllMovements(Account account) {
        // Get ALL movements for the account
        return movementRepository.findByAccountIdOrderByMovementDateDesc(account.getId())
                .map(this::mapMovementToItem)
                .collectList()
                .map(movements -> AccountStatementResponse.AccountStatementItem.builder()
                        .accountNumber(account.getAccountNumber())
                        .accountType(account.getAccountType().name())
                        .initialBalance(account.getInitialBalance())
                        .currentBalance(account.getCurrentBalance())
                        .active(account.getActive())
                        .movements(movements)
                        .build());
    }

    /**
     * Map Movement entity to MovementItem DTO.
     *
     * @param movement Movement entity
     * @return MovementItem DTO
     */
    private AccountStatementResponse.MovementItem mapMovementToItem(Movement movement) {
        return AccountStatementResponse.MovementItem.builder()
                .date(movement.getMovementDate())
                .type(movement.getMovementType().name())
                .amount(movement.getAmount())
                .balance(movement.getBalance())
                .description(movement.getDescription())
                .build();
    }
}
