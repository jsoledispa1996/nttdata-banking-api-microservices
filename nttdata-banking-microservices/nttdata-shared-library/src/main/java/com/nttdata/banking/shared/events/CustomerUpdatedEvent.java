package com.nttdata.banking.shared.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Event published when a customer is updated.
 * Allows other services to synchronize customer data.
 */
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CustomerUpdatedEvent extends BaseEvent {

    private Long customerId;
    private String customerCode;
    private String name;
    private String identification;
    private Boolean active;


}