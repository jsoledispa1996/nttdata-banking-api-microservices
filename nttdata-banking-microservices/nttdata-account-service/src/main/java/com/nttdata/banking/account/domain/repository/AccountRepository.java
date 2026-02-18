package com.nttdata.banking.account.domain.repository;

import com.nttdata.banking.account.domain.model.Account;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRepository extends R2dbcRepository<Account, Long> {

    /**
     * Find account by account number.
     */
    Mono<Account> findByAccountNumber(String accountNumber);

    /**
     * Check if account number exists.
     */
    Mono<Boolean> existsByAccountNumber(String accountNumber);

    /**
     * Find all accounts by customer ID.
     */
    Flux<Account> findByCustomerId(String customerId);

    /**
     * Find all active accounts by customer ID.
     */
    Flux<Account> findByCustomerIdAndActiveTrue(String customerId);

    /**
     * Find all active accounts.
     */
    Flux<Account> findByActiveTrue();
}