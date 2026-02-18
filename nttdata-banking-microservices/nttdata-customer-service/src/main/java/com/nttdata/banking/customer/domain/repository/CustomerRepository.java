package com.nttdata.banking.customer.domain.repository;


import com.nttdata.banking.customer.domain.model.Customer;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends R2dbcRepository<Customer, Long> {

    /**
     * Find customer by customer code.
     *
     * @param customerCode Unique customer code
     * @return Mono of Customer or empty if not found
     */
    Mono<Customer> findByCustomerCode(String customerCode);

    /**
     * Check if customer code already exists.
     *
     * @param customerCode Customer code to check
     * @return Mono of Boolean
     */
    Mono<Boolean> existsByCustomerCode(String customerCode);

    /**
     * Find all active customers.
     *
     * @return Flux of active customers
     */
    Flux<Customer> findByActiveTrue();

    /**
     * Find customer by person ID.
     *
     * @param personId Person ID
     * @return Mono of Customer or empty if not found
     */
    Mono<Customer> findByPersonId(Long personId);

    /**
     * Custom query to get customer with person details joined.
     *
     * @param id Customer ID
     * @return Mono of Customer with Person details
     */
    @Query("SELECT c.*, p.* FROM customers c " +
            "INNER JOIN persons p ON c.person_id = p.id " +
            "WHERE c.id = :id")
    Mono<Customer> findByIdWithPerson(Long id);
}
