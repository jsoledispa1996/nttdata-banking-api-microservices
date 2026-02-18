package com.nttdata.banking.account.infrastructure.rest;

import com.nttdata.banking.account.application.service.AccountService;
import com.nttdata.banking.account.infrastructure.rest.dto.AccountResponse;
import com.nttdata.banking.account.infrastructure.rest.dto.CreateAccountRequest;
import com.nttdata.banking.account.infrastructure.rest.dto.UpdateAccountRequest;
import com.nttdata.banking.shared.utils.CorrelationIdUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    /**
     * Constructor injection.
     */
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Create a new account (F1: CREATE).
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        String correlationId = CorrelationIdUtil.generateCorrelationId();
        log.info("[{}] POST /api/v1/accounts - Creando cuenta", correlationId);

        return accountService.createAccount(request)
                .doOnSuccess(response -> log.info("[{}] Cuenta creada exitosamente: {}",
                        correlationId, response.getAccountNumber()))
                .doFinally(signalType -> CorrelationIdUtil.clear());
    }

    /**
     * Get account by ID (F1: READ).
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<AccountResponse> getAccountById(@PathVariable Long id) {
        String correlationId = CorrelationIdUtil.generateCorrelationId();
        log.info("[{}] GET /api/v1/accounts/{} - Obteniendo cuenta", correlationId, id);

        return accountService.getAccountById(id)
                .doFinally(signalType -> CorrelationIdUtil.clear());
    }

    /**
     * Get all accounts (F1: READ).
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<AccountResponse> getAllAccounts(
            @RequestParam(required = false) String customerCode) {
        String correlationId = CorrelationIdUtil.generateCorrelationId();
        log.info("[{}] GET /api/v1/accounts - Obteniendo cuentas", correlationId);

        if (customerCode != null) {
            return accountService.getAccountsByCustomer(customerCode)
                    .doFinally(signalType -> CorrelationIdUtil.clear());
        }

        return accountService.getAllAccounts()
                .doFinally(signalType -> CorrelationIdUtil.clear());
    }

    /**
     * Get accounts by customer code.
     */
    @GetMapping(value = "/customer/{customerCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<AccountResponse> getAccountsByCustomerCode(@PathVariable String customerCode) {
        String correlationId = CorrelationIdUtil.generateCorrelationId();
        log.info("[{}] GET /api/v1/accounts/customer/{} - Obteniendo cuentas por cliente", correlationId, customerCode);

        return accountService.getAccountsByCustomer(customerCode)
                .doFinally(signalType -> CorrelationIdUtil.clear());
    }

    /**
     * Update account (F1: UPDATE).
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<AccountResponse> updateAccount(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAccountRequest request) {
        String correlationId = CorrelationIdUtil.generateCorrelationId();
        log.info("[{}] PUT /api/v1/accounts/{} - Actualizando cuenta", correlationId, id);

        return accountService.updateAccount(id, request)
                .doOnSuccess(response -> log.info("[{}] Cuenta actualizada exitosamente: {}",
                        correlationId, response.getAccountNumber()))
                .doFinally(signalType -> CorrelationIdUtil.clear());
    }

    /**
     * Delete account (F1: DELETE).
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAccount(@PathVariable Long id) {
        String correlationId = CorrelationIdUtil.generateCorrelationId();
        log.info("[{}] DELETE /api/v1/accounts/{} - Eliminando cuenta", correlationId, id);

        return accountService.deleteAccount(id)
                .doOnSuccess(v -> log.info("[{}] Cuenta eliminada exitosamente", correlationId))
                .doFinally(signalType -> CorrelationIdUtil.clear());
    }
}
