package com.nttdata.banking.account.infrastructure.messaging;

import com.nttdata.banking.shared.events.BaseEvent;
import com.nttdata.banking.shared.events.CustomerCreatedEvent;
import com.nttdata.banking.shared.events.CustomerDeletedEvent;
import com.nttdata.banking.shared.events.CustomerUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomerEventConsumer {

    /**
     * Handle customer created event.
     */
    @KafkaListener(topics = "${kafka.topics.customer-events}",
            groupId = "account-service-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void handleCustomerEvent(BaseEvent event) {
        if (event instanceof CustomerCreatedEvent) {
            handleCustomerCreated((CustomerCreatedEvent) event);
        } else if (event instanceof CustomerUpdatedEvent) {
            handleCustomerUpdated((CustomerUpdatedEvent) event);
        } else if (event instanceof CustomerDeletedEvent) {
            handleCustomerDeleted((CustomerDeletedEvent) event);
        } else {
            log.warn("Recibido tipo de evento desconocido: {}", event.getClass().getSimpleName());
        }
    }

    /**
     * Process customer created event.
     * Store customer information for validation when creating accounts.
     */
    private void handleCustomerCreated(CustomerCreatedEvent event) {
        log.info("Evento de cliente creado recibido: customerCode={}, name={}",
                event.getCustomerCode(), event.getName());

        log.info("Cliente {} ahora está disponible para creación de cuentas", event.getCustomerCode());
    }

    /**
     * Process customer updated event.
     * Update cached customer information.
     */
    private void handleCustomerUpdated(CustomerUpdatedEvent event) {
        log.info("Evento de cliente actualizado recibido: customerCode={}, active={}",
                event.getCustomerCode(), event.getActive());

        if (Boolean.FALSE.equals(event.getActive())) {
            log.warn("Cliente {} ha sido desactivado. Considere desactivar sus cuentas.",
                    event.getCustomerCode());
        }
    }

    /**
     * Process customer deleted event.
     * Deactivate or handle customer's accounts.
     */
    private void handleCustomerDeleted(CustomerDeletedEvent event) {
        log.info("Evento de cliente eliminado recibido: customerCode={}", event.getCustomerCode());

        log.warn("Cliente {} ha sido eliminado. Sus cuentas deberían ser desactivadas.",
                event.getCustomerCode());
    }
}
