package com.nttdata.banking.shared.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "eventType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CustomerCreatedEvent.class, name = "CUSTOMER_CREATED"),
        @JsonSubTypes.Type(value = CustomerUpdatedEvent.class, name = "CUSTOMER_UPDATED"),
        @JsonSubTypes.Type(value = CustomerDeletedEvent.class, name = "CUSTOMER_DELETED")
})
public abstract class BaseEvent {

    private String eventId;
    private String eventType;
    private LocalDateTime timestamp;
    private String correlationId;

}