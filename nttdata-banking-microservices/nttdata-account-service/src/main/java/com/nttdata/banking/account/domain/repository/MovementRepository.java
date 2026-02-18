package com.nttdata.banking.account.domain.repository;

import com.nttdata.banking.account.domain.model.Movement;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


@Repository
public interface MovementRepository extends R2dbcRepository<Movement, Long> {

    /**
     * Find all movements by account ID.
     */
    Flux<Movement> findByAccountIdOrderByMovementDateDesc(Long accountId);

    /**
     * Find movements by account ID within a date range.
     * Used for account statement reports.
     */
    Flux<Movement> findByAccountIdAndMovementDateBetweenOrderByMovementDateDesc(
            Long accountId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get the last movement for an account (most recent balance).
     * Used to get current balance before creating new movements.
     */
    @Query("SELECT * FROM ba_movimientos WHERE mo_id_cuenta = :accountId ORDER BY mo_fecha DESC, mo_id_movimiento DESC LIMIT 1")
    Mono<Movement> findLastMovementByAccountId(Long accountId);

    /**
     * Count movements by account ID.
     */
    Mono<Long> countByAccountId(Long accountId);

    /**
     * Find movements by account number (for reports).
     * Joins with ba_cuentas table to find movements by account number.
     */
    @Query("""
            SELECT m.* FROM ba_movimientos m 
            INNER JOIN ba_cuentas a ON m.mo_id_cuenta = a.cu_id_cuenta 
            WHERE a.cu_numero_cuenta = :accountNumber 
            ORDER BY m.mo_fecha DESC
            """)
    Flux<Movement> findByAccountNumber(String accountNumber);
}
