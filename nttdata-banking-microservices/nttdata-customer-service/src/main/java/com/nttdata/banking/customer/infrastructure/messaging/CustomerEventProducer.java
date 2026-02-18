package com.nttdata.banking.customer.infrastructure.messaging;

import com.nttdata.banking.shared.events.CustomerCreatedEvent;
import com.nttdata.banking.shared.events.CustomerDeletedEvent;
import com.nttdata.banking.shared.events.CustomerUpdatedEvent;
import com.nttdata.banking.shared.utils.CorrelationIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
public class CustomerEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topic;

    public CustomerEventProducer(
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${kafka.topics.customer-events}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    /**
     * Publish customer created event.
     *
     * @param event Customer created event
     * @return Mono<Void>
     */
    public Mono<Void> publishCustomerCreatedEvent(CustomerCreatedEvent event) {
        return Mono.fromRunnable(() -> {
            event.setEventId(UUID.randomUUID().toString());
            event.setCorrelationId(CorrelationIdUtil.getCorrelationId());

            kafkaTemplate.send(topic, event.getCustomerCode(), event)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Error al publicar evento CustomerCreatedEvent para cliente: {}",
                                    event.getCustomerCode(), ex);
                        } else {
                            log.info("Publicado evento CustomerCreatedEvent para cliente: {} en topic: {}",
                                    event.getCustomerCode(), topic);
                        }
                    });
        });
    }

    /**
     * Publish customer updated event.
     *
     * @param event Customer updated event
     * @return Mono<Void>
     */
    public Mono<Void> publishCustomerUpdatedEvent(CustomerUpdatedEvent event) {
        return Mono.fromRunnable(() -> {
            event.setEventId(UUID.randomUUID().toString());
            event.setCorrelationId(CorrelationIdUtil.getCorrelationId());

            kafkaTemplate.send(topic, event.getCustomerCode(), event)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Error al publicar evento CustomerUpdatedEvent para cliente: {}",
                                    event.getCustomerCode(), ex);
                        } else {
                            log.info("Publicado evento CustomerUpdatedEvent para cliente: {} en topic: {}",
                                    event.getCustomerCode(), topic);
                        }
                    });
        });
    }

    /**
     * Publish customer deleted event.
     *
     * @param event Customer deleted event
     * @return Mono<Void>
     */
    public Mono<Void> publishCustomerDeletedEvent(CustomerDeletedEvent event) {
        return Mono.fromRunnable(() -> {
            event.setEventId(UUID.randomUUID().toString());
            event.setCorrelationId(CorrelationIdUtil.getCorrelationId());

            kafkaTemplate.send(topic, event.getCustomerCode(), event)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Error al publicar evento CustomerDeletedEvent para cliente: {}",
                                    event.getCustomerCode(), ex);
                        } else {
                            log.info("Publicado evento CustomerDeletedEvent para cliente: {} en topic: {}",
                                    event.getCustomerCode(), topic);
                        }
                    });
        });
    }
}
