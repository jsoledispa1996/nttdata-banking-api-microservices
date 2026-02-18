package com.nttdata.banking.customer.application.mapper;

import com.nttdata.banking.customer.domain.model.Customer;
import com.nttdata.banking.customer.domain.model.Person;
import com.nttdata.banking.customer.infrastructure.rest.dto.CreateCustomerRequest;
import com.nttdata.banking.customer.infrastructure.rest.dto.CustomerResponse;
import com.nttdata.banking.customer.infrastructure.rest.dto.UpdateCustomerRequest;
import org.springframework.stereotype.Component;


@Component
public class CustomerMapper {

    /**
     * Convert CreateCustomerRequest to Person entity.
     *
     * @param request Create customer request
     * @return Person entity
     */
    public Person toPerson(CreateCustomerRequest request) {
        return Person.builder()
                .name(request.getName())
                .gender(request.getGender())
                .age(request.getAge())
                .identification(request.getIdentification())
                .address(request.getAddress())
                .phone(request.getPhone())
                .createdAt(java.time.LocalDateTime.now())
                .updatedAt(java.time.LocalDateTime.now())
                .build();
    }

    /**
     * Convert Customer and Person entities to CustomerResponse DTO.
     *
     * @param customer Customer entity
     * @param person Person entity
     * @return CustomerResponse DTO
     */
    public CustomerResponse toResponse(Customer customer, Person person) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .customerCode(customer.getCustomerCode())
                .active(customer.getActive())
                .name(person.getName())
                .gender(person.getGender())
                .age(person.getAge())
                .identification(person.getIdentification())
                .address(person.getAddress())
                .phone(person.getPhone())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }

    /**
     * Update Person entity from UpdateCustomerRequest.
     * Only updates non-null fields.
     *
     * @param person Existing person entity
     * @param request Update request
     */
    public void updatePersonFromRequest(Person person, UpdateCustomerRequest request) {
        if (request.getName() != null) {
            person.setName(request.getName());
        }
        if (request.getGender() != null) {
            person.setGender(request.getGender());
        }
        if (request.getAge() != null) {
            person.setAge(request.getAge());
        }
        if (request.getAddress() != null) {
            person.setAddress(request.getAddress());
        }
        if (request.getPhone() != null) {
            person.setPhone(request.getPhone());
        }
    }
}
