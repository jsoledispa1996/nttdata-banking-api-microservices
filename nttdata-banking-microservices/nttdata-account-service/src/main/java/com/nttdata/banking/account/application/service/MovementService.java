package com.nttdata.banking.account.application.service;


import com.nttdata.banking.account.application.mapper.MovementMapper;
import com.nttdata.banking.account.domain.model.Account;
import com.nttdata.banking.account.domain.model.Movement;
import com.nttdata.banking.account.domain.model.MovementType;
import com.nttdata.banking.account.domain.repository.AccountRepository;
import com.nttdata.banking.account.domain.repository.MovementRepository;
import com.nttdata.banking.account.infrastructure.rest.dto.CreateMovementRequest;
import com.nttdata.banking.account.infrastructure.rest.dto.MovementResponse;
import com.nttdata.banking.shared.exceptions.BusinessException;
import com.nttdata.banking.shared.exceptions.InsufficientBalanceException;
import com.nttdata.banking.shared.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
public class MovementService {

    private final MovementRepository movementRepository;
    private final AccountRepository accountRepository;
    private final MovementMapper movementMapper;

    /**
     * Constructor injection for all dependencies.
     */
    public MovementService(
            MovementRepository movementRepository,
            AccountRepository accountRepository,
            MovementMapper movementMapper) {
        this.movementRepository = movementRepository;
        this.accountRepository = accountRepository;
        this.movementMapper = movementMapper;
    }

    /**
     * Create a new movement (F1: CREATE, F2: Business logic, F3: Validation).
     */
    @Transactional
    public Mono<MovementResponse> createMovement(CreateMovementRequest request) {
        log.info("Creando movimiento {} para cuenta: {} con monto: {}",
                request.getMovementType(), request.getAccountNumber(), request.getAmount());

        // Validate amount > 0 (F2)
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return Mono.error(new BusinessException(
                    "El monto debe ser mayor que 0",
                    "INVALID_AMOUNT"));
        }

        // Find account
        return accountRepository.findByAccountNumber(request.getAccountNumber())
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cuenta", request.getAccountNumber())))
                .flatMap(account -> {
                    // Validate account is active
                    if (!Boolean.TRUE.equals(account.getActive())) {
                        return Mono.error(new BusinessException(
                                "La cuenta no está activa",
                                "INACTIVE_ACCOUNT"));
                    }

                    // Calculate new balance based on movement type (F2)
                    BigDecimal newBalance;
                    if ("RETIRO".equals(request.getMovementType())) {
                        // RETIRO: Subtract from balance (F2)
                        newBalance = account.getCurrentBalance().subtract(request.getAmount());

                        // F3: Validate sufficient balance - "Saldo no disponible"
                        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                            log.warn("Saldo insuficiente para cuenta: {}. Actual: {}, Solicitado: {}",
                                    request.getAccountNumber(), account.getCurrentBalance(), request.getAmount());
                            return Mono.error(new InsufficientBalanceException(request.getAccountNumber()));
                        }
                    } else if ("DEPOSITO".equals(request.getMovementType())) {
                        // DEPOSITO: Add to balance (F2)
                        newBalance = account.getCurrentBalance().add(request.getAmount());
                    } else {
                        return Mono.error(new BusinessException(
                                "Tipo de movimiento inválido. Debe ser DEPOSITO o RETIRO",
                                "INVALID_MOVEMENT_TYPE"));
                    }

                    // Create movement entity
                    Movement movement = Movement.builder()
                            .movementDate(LocalDateTime.now())
                            .movementType(MovementType.valueOf(request.getMovementType()))
                            .amount(request.getAmount())
                            .balance(newBalance) // Balance after this movement
                            .description(request.getDescription())
                            .accountId(account.getId())
                            .build();

                    // Update account balance
                    account.setCurrentBalance(newBalance);

                    // Save movement and update account atomically
                    return accountRepository.save(account)
                            .then(movementRepository.save(movement))
                            .map(savedMovement -> movementMapper.toResponse(savedMovement, account))
                            .doOnSuccess(response -> log.info(
                                    "Movement created successfully. Account: {}, Type: {}, New Balance: {}",
                                    request.getAccountNumber(), request.getMovementType(), newBalance));
                });
    }

    /**
     * Get movement by ID (F1: READ).
     */
    public Mono<MovementResponse> getMovementById(Long id) {
        log.info("Obteniendo movimiento por ID: {}", id);

        return movementRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Movimiento", id.toString())))
                .flatMap(movement -> accountRepository.findById(movement.getAccountId())
                        .map(account -> movementMapper.toResponse(movement, account)));
    }

    /**
     * Get all movements (F1: READ).
     */
    public Flux<MovementResponse> getAllMovements() {
        log.info("Obteniendo todos los movimientos");

        return movementRepository.findAll()
                .flatMap(movement -> accountRepository.findById(movement.getAccountId())
                        .map(account -> movementMapper.toResponse(movement, account)));
    }

    /**
     * Get movements by account number.
     */
    public Flux<MovementResponse> getMovementsByAccount(String accountNumber) {
        log.info("Obteniendo movimientos para cuenta: {}", accountNumber);

        return accountRepository.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cuenta", accountNumber)))
                .flatMapMany(account -> movementRepository.findByAccountIdOrderByMovementDateDesc(account.getId())
                        .map(movement -> movementMapper.toResponse(movement, account)));
    }

    /**
     * Get movements by customer code (all movements from all customer accounts).
     */
    public Flux<MovementResponse> getMovementsByCustomerCode(String customerCode) {
        log.info("Obteniendo movimientos para cliente: {}", customerCode);

        return accountRepository.findByCustomerId(customerCode)
                .switchIfEmpty(Flux.error(new ResourceNotFoundException("Cliente", customerCode)))
                .flatMap(account -> movementRepository.findByAccountIdOrderByMovementDateDesc(account.getId())
                        .map(movement -> movementMapper.toResponse(movement, account)));
    }

    /**
     * Delete movement (F1: DELETE).
     */
    @Transactional
    public Mono<Void> deleteMovement(Long id) {
        log.warn("Eliminando movimiento ID: {} - ¡Esto no debería hacerse en producción!", id);

        return movementRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Movimiento", id.toString())))
                .flatMap(movement -> movementRepository.deleteById(id)
                        .doOnSuccess(v -> log.info("Movimiento eliminado: {}", id)));
    }
}

