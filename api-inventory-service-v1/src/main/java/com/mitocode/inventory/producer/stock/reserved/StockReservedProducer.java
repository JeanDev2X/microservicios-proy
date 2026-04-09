package com.mitocode.inventory.producer.stock.reserved;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class StockReservedProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public StockReservedProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void send(Object event) {
        try {
            // Convertimos el objeto a String antes de enviarlo
            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("stock-reserved-topic", message);
            System.out.println("Evento StockReserved enviado a Kafka");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
