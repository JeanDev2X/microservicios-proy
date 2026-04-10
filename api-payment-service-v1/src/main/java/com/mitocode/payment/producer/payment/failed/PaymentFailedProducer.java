package com.mitocode.payment.producer.payment.failed;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitocode.payment.producer.payment.failed.event.PaymentFailedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentFailedProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public PaymentFailedProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void     send(PaymentFailedEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("payment-failed-topic", message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
