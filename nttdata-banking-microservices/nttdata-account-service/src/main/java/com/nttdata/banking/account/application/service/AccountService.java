package com.nttdata.banking.account.application.service;


import com.nttdata.banking.account.application.mapper.AccountMapper;
import com.nttdata.banking.account.domain.model.Account;
import com.nttdata.banking.account.domain.repository.AccountRepository;
import com.nttdata.banking.account.domain.repository.MovementRepository;
import com.nttdata.banking.account.infrastructure.rest.dto.AccountResponse;
import com.nttdata.banking.account.infrastructure.rest.dto.CreateAccountRequest;
import com.nttdata.banking.account.infrastructure.rest.dto.UpdateAccountRequest;
import com.nttdata.banking.shared.exceptions.BusinessException;
import com.nttdata.banking.shared.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final MovementRepository movementRepository;
    private final AccountMapper accountMapper;

    /**
     * Constructor injection for all dependencies.
     */
    public AccountService(
            AccountRepository accountRepository,
            MovementRepository movementRepository,
            AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.movementRepository = movementRepository;
        this.accountMapper = accountMapper;
    }

    /**
     * Create a new account (F1: CREATE).
     */
    @Transactional
    public Mono<AccountResponse> createAccount(CreateAccountRequest request) {
        log.info("Creando cuenta con número: {}", request.getAccountNumber());

        return accountRepository.existsByAccountNumber(request.getAccountNumber())
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new BusinessException(
                                "La cuenta con número " + request.getAccountNumber() + " ya existe",
                                "DUPLICATE_ACCOUNT_NUMBER"));
                    }

                    Account account = accountMapper.toEntity(request);

                    return accountRepository.save(account)
                            .map(accountMapper::toResponse)
                            .doOnSuccess(response -> log.info("Cuenta creada exitosamente: {}", response.getAccountNumber()));
                });
    }

    /**
     * Get account by ID (F1: READ).
     */
    public Mono<AccountResponse> getAccountById(Long id) {
        log.info("Obteniendo cuenta por ID: {}", id);

        return accountRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cuenta", id.toString())))
                .map(accountMapper::toResponse);
    }

    /**
     * Get account by account number.
     */
    public Mono<AccountResponse> getAccountByNumber(String accountNumber) {
        log.info("Obteniendo cuenta por número: {}", accountNumber);

        return accountRepository.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cuenta", accountNumber)))
                .map(accountMapper::toResponse);
    }

    /**
     * Get all accounts (F1: READ).
     */
    public Flux<AccountResponse> getAllAccounts() {
        log.info("Obteniendo todas las cuentas");

        return accountRepository.findAll()
                .map(accountMapper::toResponse);
    }

    /**
     * Get accounts by customer code.
     *
     * @param customerCode Customer code
     * @return Flux of AccountResponse
     */
    public Flux<AccountResponse> getAccountsByCustomer(String customerCode) {
        log.info("Obteniendo cuentas para cliente: {}", customerCode);

        return accountRepository.findByCustomerId(customerCode)
                .map(accountMapper::toResponse);
    }

    /**
     * Update account (F1: UPDATE).
     */
    @Transactional
    public Mono<AccountResponse> updateAccount(Long id, UpdateAccountRequest request) {
        log.info("Actualizando cuenta ID: {}", id);

        return accountRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cuenta", id.toString())))
                .flatMap(account -> {
                    if (request.getCurrentBalance() != null) {
                        account.setCurrentBalance(request.getCurrentBalance());
                    }
                    if (request.getActive() != null) {
                        account.setActive(request.getActive());
                    }

                    return accountRepository.save(account)
                            .map(accountMapper::toResponse)
                            .doOnSuccess(response -> log.info("Cuenta actualizada exitosamente: {}", response.getAccountNumber()));
                });
    }

    /**
     * Delete account (F1: DELETE).
     * Cannot delete accounts with movements.
     */
    @Transactional
    public Mono<Void> deleteAccount(Long id) {
        log.info("Eliminando cuenta ID: {}", id);

        return accountRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cuenta", id.toString())))
                .flatMap(account ->
                        // Check if account has movements
                        movementRepository.findByAccountIdOrderByMovementDateDesc(account.getId())
                                .hasElements()
                                .flatMap(hasMovements -> {
                                    if (Boolean.TRUE.equals(hasMovements)) {
                                        return Mono.error(new BusinessException(
                                                "No se puede eliminar una cuenta con movimientos asociados. Cuenta: " + account.getAccountNumber(),
                                                "ACCOUNT_HAS_MOVEMENTS"));
                                    }
                                    return accountRepository.deleteById(id)
                                            .doOnSuccess(v -> log.info("Cuenta eliminada exitosamente: {}", account.getAccountNumber()));
                                })
                );
    }
}
