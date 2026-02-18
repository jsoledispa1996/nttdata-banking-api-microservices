package com.nttdata.banking.account.infrastructure.rest;

import com.nttdata.banking.account.application.service.MovementService;
import com.nttdata.banking.account.infrastructure.rest.dto.CreateMovementRequest;
import com.nttdata.banking.account.infrastructure.rest.dto.MovementResponse;
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
@RequestMapping("/api/v1/movements")
public class MovementController {

    private final MovementService movementService;

    /**
     * Constructor injection.
     */
    public MovementController(MovementService movementService) {
        this.movementService = movementService;
    }

    /**
     * Create a new movement (F2 and F3 requirements).
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovementResponse> createMovement(@Valid @RequestBody CreateMovementRequest request) {
        String correlationId = CorrelationIdUtil.generateCorrelationId();
        log.info("[{}] POST /api/v1/movements - Creando movimiento {} de {} para cuenta {}",
                correlationId, request.getMovementType(), request.getAmount(), request.getAccountNumber());

        return movementService.createMovement(request)
                .doOnSuccess(response -> log.info("[{}] Movimiento creado exitosamente: balance={}",
                        correlationId, response.getBalance()))
                .doFinally(signalType -> CorrelationIdUtil.clear());
    }

    /**
     * Get movement by ID.
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<MovementResponse> getMovementById(@PathVariable Long id) {
        String correlationId = CorrelationIdUtil.generateCorrelationId();
        log.info("[{}] GET /api/v1/movements/{} - Obteniendo movimiento", correlationId, id);

        return movementService.getMovementById(id)
                .doFinally(signalType -> CorrelationIdUtil.clear());
    }

    /**
     * Get all movements.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<MovementResponse> getAllMovements() {
        String correlationId = CorrelationIdUtil.generateCorrelationId();
        log.info("[{}] GET /api/v1/movements - Obteniendo todos los movimientos", correlationId);

        return movementService.getAllMovements()
                .doFinally(signalType -> CorrelationIdUtil.clear());
    }

    /**
     * Get movements by account ID.
     */
    @GetMapping(value = "/account/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<MovementResponse> getMovementsByAccount(@PathVariable Long accountId) {
        String correlationId = CorrelationIdUtil.generateCorrelationId();
        log.info("[{}] GET /api/v1/movements/account/{} - Obteniendo movimientos por cuenta",
                correlationId, accountId);

        return movementService.getMovementsByAccount(accountId.toString())
                .doFinally(signalType -> CorrelationIdUtil.clear());
    }

    /**
     * Get movements by customer code (all movements from all customer accounts).
     */
    @GetMapping(value = "/customer/{customerCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<MovementResponse> getMovementsByCustomer(@PathVariable String customerCode) {
        String correlationId = CorrelationIdUtil.generateCorrelationId();
        log.info("[{}] GET /api/v1/movements/customer/{} - Obteniendo movimientos por cliente",
                correlationId, customerCode);

        return movementService.getMovementsByCustomerCode(customerCode)
                .doFinally(signalType -> CorrelationIdUtil.clear());
    }

    /**
     * Delete movement.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteMovement(@PathVariable Long id) {
        String correlationId = CorrelationIdUtil.generateCorrelationId();
        log.info("[{}] DELETE /api/v1/movements/{} - Eliminando movimiento", correlationId, id);

        return movementService.deleteMovement(id)
                .doOnSuccess(v -> log.info("[{}] Movimiento eliminado exitosamente", correlationId))
                .doFinally(signalType -> CorrelationIdUtil.clear());
    }
}
