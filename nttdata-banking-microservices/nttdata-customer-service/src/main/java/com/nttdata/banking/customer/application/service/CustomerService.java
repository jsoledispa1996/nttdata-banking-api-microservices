package com.nttdata.banking.customer.application.service;

import com.nttdata.banking.customer.application.mapper.CustomerMapper;
import com.nttdata.banking.customer.domain.model.Customer;
import com.nttdata.banking.customer.domain.model.Person;
import com.nttdata.banking.customer.domain.repository.CustomerRepository;
import com.nttdata.banking.customer.domain.repository.PersonRepository;
import com.nttdata.banking.customer.infrastructure.messaging.CustomerEventProducer;
import com.nttdata.banking.customer.infrastructure.rest.dto.CreateCustomerRequest;
import com.nttdata.banking.customer.infrastructure.rest.dto.CustomerResponse;
import com.nttdata.banking.customer.infrastructure.rest.dto.UpdateCustomerRequest;
import com.nttdata.banking.shared.events.CustomerCreatedEvent;
import com.nttdata.banking.shared.events.CustomerDeletedEvent;
import com.nttdata.banking.shared.events.CustomerUpdatedEvent;
import com.nttdata.banking.shared.exceptions.BusinessException;
import com.nttdata.banking.shared.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;


@Slf4j
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PersonRepository personRepository;
    private final CustomerMapper customerMapper;
    private final CustomerEventProducer eventProducer;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Constructor injection for all dependencies.
     */
    public CustomerService(
            CustomerRepository customerRepository,
            PersonRepository personRepository,
            CustomerMapper customerMapper,
            CustomerEventProducer eventProducer) {
        this.customerRepository = customerRepository;
        this.personRepository = personRepository;
        this.customerMapper = customerMapper;
        this.eventProducer = eventProducer;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Create a new customer (F1: CREATE).
     */
    @Transactional
    public Mono<CustomerResponse> createCustomer(CreateCustomerRequest request) {
        log.info("Creando cliente con identificación: {}", request.getIdentification());

        return personRepository.existsByIdentification(request.getIdentification())
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new BusinessException(
                                "El cliente con identificación " + request.getIdentification() + " ya existe",
                                "DUPLICATE_IDENTIFICATION"));
                    }

                    // Validate customerCode doesn't exist
                    return customerRepository.existsByCustomerCode(request.getCustomerCode())
                            .flatMap(codeExists -> {
                                if (Boolean.TRUE.equals(codeExists)) {
                                    return Mono.error(new BusinessException(
                                            "El código de cliente " + request.getCustomerCode() + " ya existe",
                                            "DUPLICATE_CUSTOMER_CODE"));
                                }

                                // Create person entity
                                Person person = customerMapper.toPerson(request);

                                return personRepository.save(person)
                                        .flatMap(savedPerson -> {
                                            // Create customer entity using the provided customerCode
                                            String encryptedPassword = passwordEncoder.encode(request.getPassword());

                                            Customer customer = Customer.builder()
                                                    .personId(savedPerson.getId())
                                                    .customerCode(request.getCustomerCode())  // Use provided code
                                                    .password(encryptedPassword)
                                                    .active(true)
                                                    .build();

                                            return customerRepository.save(customer)
                                        .flatMap(savedCustomer -> {
                                            // Publish event to Kafka
                                            CustomerCreatedEvent event = CustomerCreatedEvent.builder()
                                                    .customerId(savedCustomer.getId())
                                                    .customerCode(savedCustomer.getCustomerCode())
                                                    .name(savedPerson.getName())
                                                    .identification(savedPerson.getIdentification())
                                                    .active(savedCustomer.getActive())
                                                    .eventType("CUSTOMER_CREATED")
                                                    .timestamp(LocalDateTime.now())
                                                    .build();

                                            return eventProducer.publishCustomerCreatedEvent(event)
                                                    .thenReturn(customerMapper.toResponse(savedCustomer, savedPerson));
                                        });
                            });
                        });
                });
    }

    /**
     * Get customer by ID (F1: READ).
     */
    public Mono<CustomerResponse> getCustomerById(Long id) {
        log.info("Obteniendo cliente por ID: {}", id);

        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cliente", id.toString())))
                .flatMap(customer -> personRepository.findById(customer.getPersonId())
                        .map(person -> customerMapper.toResponse(customer, person)));
    }

    /**
     * Get customer by customer code.
     */
    public Mono<CustomerResponse> getCustomerByCustomerCode(String customerCode) {
        log.info("Obteniendo cliente por código: {}", customerCode);

        return customerRepository.findByCustomerCode(customerCode)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cliente", customerCode)))
                .flatMap(customer -> personRepository.findById(customer.getPersonId())
                        .map(person -> customerMapper.toResponse(customer, person)));
    }

    /**
     * Get all customers (F1: READ).
     */
    public Flux<CustomerResponse> getAllCustomers() {
        log.info("Obteniendo todos los clientes");

        return customerRepository.findAll()
                .flatMap(customer -> personRepository.findById(customer.getPersonId())
                        .map(person -> customerMapper.toResponse(customer, person)));
    }

    /**
     * Update customer (F1: UPDATE).
     */
    @Transactional
    public Mono<CustomerResponse> updateCustomer(Long id, UpdateCustomerRequest request) {
        log.info("Actualizando cliente ID: {}", id);

        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cliente", id.toString())))
                .flatMap(customer -> personRepository.findById(customer.getPersonId())
                        .flatMap(person -> {
                            // Update person fields
                            customerMapper.updatePersonFromRequest(person, request);

                            // Update customer fields
                            if (request.getPassword() != null) {
                                customer.setPassword(passwordEncoder.encode(request.getPassword()));
                            }
                            if (request.getActive() != null) {
                                customer.setActive(request.getActive());
                            }

                            return personRepository.save(person)
                                    .then(customerRepository.save(customer))
                                    .flatMap(updatedCustomer -> {
                                        // Publish event to Kafka
                                        CustomerUpdatedEvent event = CustomerUpdatedEvent.builder()
                                                .customerId(updatedCustomer.getId())
                                                .customerCode(updatedCustomer.getCustomerCode())
                                                .name(person.getName())
                                                .identification(person.getIdentification())
                                                .active(updatedCustomer.getActive())
                                                .eventType("CUSTOMER_UPDATED")
                                                .build();

                                        return eventProducer.publishCustomerUpdatedEvent(event)
                                                .thenReturn(customerMapper.toResponse(updatedCustomer, person));
                                    });
                        }));
    }

    /**
     * Delete customer (F1: DELETE).
     */
    @Transactional
    public Mono<Void> deleteCustomer(Long id) {
        log.info("Eliminando cliente ID: {}", id);

        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cliente", id.toString())))
                .flatMap(customer -> {
                    // Publish event to Kafka before deletion
                    CustomerDeletedEvent event = CustomerDeletedEvent.builder()
                            .customerId(customer.getId())
                            .customerCode(customer.getCustomerCode())
                            .eventType("CUSTOMER_DELETED")
                            .build();

                    return eventProducer.publishCustomerDeletedEvent(event)
                            .then(customerRepository.deleteById(id))
                            .then(personRepository.deleteById(customer.getPersonId()));
                });
    }


}