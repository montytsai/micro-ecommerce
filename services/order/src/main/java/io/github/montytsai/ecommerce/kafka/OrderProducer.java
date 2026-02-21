package io.github.montytsai.ecommerce.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, OrderConfirmation> kafkaTemplate;

    public void sendOrderMessage(OrderConfirmation confirmation) {
        log.info("Sending order confirmation for Order: {}", confirmation.orderReference());
        kafkaTemplate.send("order-topic", confirmation)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Message sent successfully to topic: {}", result.getRecordMetadata().topic());
                    } else {
                        log.error("Failed to send message", ex);
                    }
                });
    }

}