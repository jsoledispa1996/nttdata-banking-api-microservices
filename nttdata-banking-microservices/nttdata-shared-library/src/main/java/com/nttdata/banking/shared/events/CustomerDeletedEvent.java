package com.nttdata.banking.shared.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Event published when a customer is deleted.
 * Account Service can use this to deactivate associated accounts.
 */
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CustomerDeletedEvent extends BaseEvent {

    private Long customerId;
    private String customerCode;

}
