package com.nttdata.banking.shared.events;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * Event published when a new customer is created.
 * Consumed by Account Service to validate customer existence before creating accounts.
 */
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CustomerCreatedEvent extends BaseEvent {

    private Long customerId;
    private String customerCode;
    private String name;
    private String identification;
    private Boolean active;

}

