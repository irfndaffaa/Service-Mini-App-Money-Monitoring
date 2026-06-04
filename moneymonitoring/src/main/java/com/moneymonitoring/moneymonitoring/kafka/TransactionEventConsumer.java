package com.moneymonitoring.moneymonitoring.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TransactionEventConsumer {

    @KafkaListener(
            topics = "${app.kafka.topic.transaction:transaction-events}",
            groupId = "${spring.kafka.consumer.group-id:money-monitoring-group}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeTransactionEvent(TransactionEvent event) {
        log.info("Consumed transaction event: type={}, id={}, amount={}, category={}",
                event.getEventType(), event.getTransactionId(), event.getAmount(), event.getCategory());
    }
}
