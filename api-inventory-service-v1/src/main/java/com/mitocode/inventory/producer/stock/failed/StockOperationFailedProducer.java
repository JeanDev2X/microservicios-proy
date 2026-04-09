package com.mitocode.inventory.producer.stock.failed;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitocode.inventory.producer.stock.failed.event.StockOperationFailedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class StockOperationFailedProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public StockOperationFailedProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void send(StockOperationFailedEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("stock-failed-topic", message);
            System.err.println("[Producer] StockOperationFailedEvent enviado: " + event.getOrderId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
