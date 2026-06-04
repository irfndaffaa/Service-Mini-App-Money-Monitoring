package com.moneymonitoring.moneymonitoring.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TransactionEventProducer {

    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;
    private final boolean kafkaAvailable;

    public TransactionEventProducer(ObjectProvider<KafkaTemplate<String, TransactionEvent>> provider) {
        this.kafkaTemplate = provider.getIfAvailable();
        this.kafkaAvailable = this.kafkaTemplate != null;
    }

    @Value("${app.kafka.topic.transaction:transaction-events}")
    private String transactionTopic;

    public void sendTransactionEvent(TransactionEvent event) {
        if (!kafkaAvailable) {
            log.debug("Kafka not available, skipping event: {}", event.getEventId());
            return;
        }
        kafkaTemplate.send(transactionTopic, event.getTransactionId(), event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Sent event [{}] with offset [{}]",
                                event.getEventId(), result.getRecordMetadata().offset());
                    } else {
                        log.error("Failed to send event [{}]: {}", event.getEventId(), ex.getMessage(), ex);
                    }
                });
    }
}
