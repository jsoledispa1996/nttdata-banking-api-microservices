package com.nttdata.banking.account.application.service;

import com.nttdata.banking.account.application.mapper.MovementMapper;
import com.nttdata.banking.account.domain.model.Account;
import com.nttdata.banking.account.domain.model.AccountType;
import com.nttdata.banking.account.domain.model.Movement;
import com.nttdata.banking.account.domain.model.MovementType;
import com.nttdata.banking.account.domain.repository.AccountRepository;
import com.nttdata.banking.account.domain.repository.MovementRepository;
import com.nttdata.banking.account.infrastructure.rest.dto.CreateMovementRequest;
import com.nttdata.banking.account.infrastructure.rest.dto.MovementResponse;
import com.nttdata.banking.shared.exceptions.InsufficientBalanceException;
import com.nttdata.banking.shared.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MovementServiceTest {

    @Mock
    private MovementRepository movementRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private MovementMapper movementMapper;

    @InjectMocks
    private MovementService movementService;

    private Account testAccount;
    private Movement testMovement;
    private CreateMovementRequest depositRequest;
    private CreateMovementRequest withdrawalRequest;
    private MovementResponse movementResponse;

    @BeforeEach
    void setUp() {
        // Setup test data
        testAccount = Account.builder()
                .id(1L)
                .accountNumber("478758")
                .accountType(AccountType.AHORRO)
                .initialBalance(new BigDecimal("2000.00"))
                .currentBalance(new BigDecimal("2000.00"))
                .active(true)
                .customerId("CUST001")
                .createdDate(LocalDateTime.now())
                .build();

        testMovement = Movement.builder()
                .id(1L)
                .movementDate(LocalDateTime.now())
                .movementType(MovementType.DEPOSITO)
                .amount(new BigDecimal("500.00"))
                .balance(new BigDecimal("2500.00"))
                .description("Test deposit")
                .accountId(1L)
                .build();

        depositRequest = CreateMovementRequest.builder()
                .accountNumber("478758")
                .movementType("DEPOSITO")
                .amount(new BigDecimal("500.00"))
                .description("Test deposit")
                .build();

        withdrawalRequest = CreateMovementRequest.builder()
                .accountNumber("478758")
                .movementType("RETIRO")
                .amount(new BigDecimal("2500.00"))
                .description("Test withdrawal - insufficient balance")
                .build();

        movementResponse = MovementResponse.builder()
                .id(1L)
                .movementDate(LocalDateTime.now())
                .movementType("DEPOSITO")
                .amount(new BigDecimal("500.00"))
                .balance(new BigDecimal("2500.00"))
                .description("Test deposit")
                .accountNumber("478758")
                .build();
    }


    @Test
    void testCreateMovement_Deposit_Success() {
        // Given
        when(accountRepository.findByAccountNumber(anyString())).thenReturn(Mono.just(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(Mono.just(testAccount));
        when(movementRepository.save(any(Movement.class))).thenReturn(Mono.just(testMovement));
        when(movementMapper.toResponse(any(Movement.class), any(Account.class))).thenReturn(movementResponse);

        // When
        Mono<MovementResponse> result = movementService.createMovement(depositRequest);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getMovementType().equals("DEPOSITO") &&
                        response.getAmount().compareTo(new BigDecimal("500.00")) == 0 &&
                        response.getAccountNumber().equals("478758")
                )
                .verifyComplete();

        // Verify interactions
        verify(accountRepository, times(1)).findByAccountNumber("478758");
        verify(accountRepository, times(1)).save(any(Account.class));
        verify(movementRepository, times(1)).save(any(Movement.class));
    }


    @Test
    void testCreateMovement_InsufficientBalance_ThrowsException() {
        // Given - Account with 2000, trying to withdraw 2500
        when(accountRepository.findByAccountNumber(anyString())).thenReturn(Mono.just(testAccount));

        // When
        Mono<MovementResponse> result = movementService.createMovement(withdrawalRequest);

        // Then - Should throw InsufficientBalanceException
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof InsufficientBalanceException &&
                        throwable.getMessage().contains("Saldo no disponible")
                )
                .verify();

        // Verify that save methods were never called
        verify(accountRepository, times(1)).findByAccountNumber("478758");
        verify(accountRepository, never()).save(any(Account.class));
        verify(movementRepository, never()).save(any(Movement.class));
    }


    @Test
    void testGetMovementById_Success() {
        // Given
        when(movementRepository.findById(anyLong())).thenReturn(Mono.just(testMovement));
        when(accountRepository.findById(anyLong())).thenReturn(Mono.just(testAccount));
        when(movementMapper.toResponse(any(Movement.class), any(Account.class))).thenReturn(movementResponse);

        // When
        Mono<MovementResponse> result = movementService.getMovementById(1L);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getId().equals(1L) &&
                        response.getAccountNumber().equals("478758")
                )
                .verifyComplete();

        verify(movementRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).findById(1L);
    }


    @Test
    void testGetMovementById_NotFound_ThrowsException() {
        // Given
        when(movementRepository.findById(anyLong())).thenReturn(Mono.empty());

        // When
        Mono<MovementResponse> result = movementService.getMovementById(999L);

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof ResourceNotFoundException &&
                        throwable.getMessage().contains("Movimiento") &&
                        throwable.getMessage().contains("999")
                )
                .verify();

        verify(movementRepository, times(1)).findById(999L);
        verify(accountRepository, never()).findById(anyLong());
    }


    @Test
    void testCreateMovement_InvalidAmount_ThrowsException() {
        // Given - Request with negative amount
        CreateMovementRequest invalidRequest = CreateMovementRequest.builder()
                .accountNumber("478758")
                .movementType("DEPOSITO")
                .amount(new BigDecimal("-100.00"))
                .description("Invalid amount")
                .build();

        // When
        Mono<MovementResponse> result = movementService.createMovement(invalidRequest);

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable.getMessage().contains("El monto debe ser mayor que 0")
                )
                .verify();

        // Verify that repository methods were never called
        verify(accountRepository, never()).findByAccountNumber(anyString());
        verify(movementRepository, never()).save(any(Movement.class));
    }
}
