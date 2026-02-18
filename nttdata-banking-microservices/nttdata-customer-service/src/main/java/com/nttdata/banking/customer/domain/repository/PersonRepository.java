package com.nttdata.banking.customer.domain.repository;

import com.nttdata.banking.customer.domain.model.Person;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PersonRepository extends R2dbcRepository<Person, Long> {

    /**
     * Find person by identification number.
     *
     * @param identification Person's identification number
     * @return Mono of Person or empty if not found
     */
    Mono<Person> findByIdentification(String identification);

    /**
     * Check if person exists by identification.
     *
     * @param identification Person's identification number
     * @return Mono of Boolean
     */
    Mono<Boolean> existsByIdentification(String identification);
}