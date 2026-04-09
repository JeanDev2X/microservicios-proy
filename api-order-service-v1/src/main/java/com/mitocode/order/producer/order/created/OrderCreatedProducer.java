package com.mitocode.order.producer.order.created;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitocode.order.producer.order.created.event.OrderCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public OrderCreatedProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void send(OrderCreatedEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("order-created-topic", message);
            System.out.println("[OrderProducer] Evento enviado: " + event.getOrderId());
        } catch (Exception e) {
            System.err.println("❌ Error en Kafka: " + e.getMessage());
        }
    }
}
