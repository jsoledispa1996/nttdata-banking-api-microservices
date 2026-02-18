package com.nttdata.banking.customer.infrastructure.rest;

import com.nttdata.banking.customer.application.service.CustomerService;
import com.nttdata.banking.customer.infrastructure.rest.dto.CreateCustomerRequest;
import com.nttdata.banking.customer.infrastructure.rest.dto.CustomerResponse;
import com.nttdata.banking.customer.infrastructure.rest.dto.UpdateCustomerRequest;
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
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Constructor injection.
     */
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Create a new customer (F1: CREATE).
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CustomerResponse> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        String correlationId = CorrelationIdUtil.generateCorrelationId();
        log.info("[{}] POST /api/v1/customers - Creando cliente", correlationId);

        return customerService.createCustomer(request)
                .doOnSuccess(response -> log.info("[{}] Cliente creado exitosamente: {}",
                        correlationId, response.getCustomerCode()))
                .doFinally(signalType -> CorrelationIdUtil.clear());
    }

    /**
     * Get customer by ID (F1: READ).
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<CustomerResponse> getCustomerById(@PathVariable Long id) {
        String correlationId = CorrelationIdUtil.generateCorrelationId();
        log.info("[{}] GET /api/v1/customers/{} - Obteniendo cliente", correlationId, id);

        return customerService.getCustomerById(id)
                .doFinally(signalType -> CorrelationIdUtil.clear());
    }

    /**
     * Get customer by customer code (F1: READ).
     */
    @GetMapping(value = "/code/{customerCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<CustomerResponse> getCustomerByCode(@PathVariable String customerCode) {
        String correlationId = CorrelationIdUtil.generateCorrelationId();
        log.info("[{}] GET /api/v1/customers/code/{} - Obteniendo cliente por código", correlationId, customerCode);

        return customerService.getCustomerByCustomerCode(customerCode)
                .doFinally(signalType -> CorrelationIdUtil.clear());
    }

    /**
     * Get all customers (F1: READ).
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<CustomerResponse> getAllCustomers() {
        String correlationId = CorrelationIdUtil.generateCorrelationId();
        log.info("[{}] GET /api/v1/customers - Obteniendo todos los clientes", correlationId);

        return customerService.getAllCustomers()
                .doOnComplete(() -> log.info("[{}] Todos los clientes obtenidos", correlationId))
                .doFinally(signalType -> CorrelationIdUtil.clear());
    }

    /**
     * Update customer (F1: UPDATE).
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<CustomerResponse> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCustomerRequest request) {
        String correlationId = CorrelationIdUtil.generateCorrelationId();
        log.info("[{}] PUT /api/v1/customers/{} - Actualizando cliente", correlationId, id);

        return customerService.updateCustomer(id, request)
                .doOnSuccess(response -> log.info("[{}] Cliente actualizado exitosamente: {}",
                        correlationId, response.getCustomerCode()))
                .doFinally(signalType -> CorrelationIdUtil.clear());
    }

    /**
     * Delete customer (F1: DELETE).
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCustomer(@PathVariable Long id) {
        String correlationId = CorrelationIdUtil.generateCorrelationId();
        log.info("[{}] DELETE /api/v1/customers/{} - Eliminando cliente", correlationId, id);

        return customerService.deleteCustomer(id)
                .doOnSuccess(v -> log.info("[{}] Cliente eliminado exitosamente", correlationId))
                .doFinally(signalType -> CorrelationIdUtil.clear());
    }
}

