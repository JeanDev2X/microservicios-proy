package com.mitocode.payment.producer.payment.completed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitocode.payment.producer.payment.completed.event.PaymentCompletedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentCompletedProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public PaymentCompletedProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void send(PaymentCompletedEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            // El tópico que definimos en el Inventario: payment-completed-topic
            kafkaTemplate.send("payment-completed-topic", message);
            System.out.println("[PaymentProducer] Evento enviado a Kafka: " + event.getOrderId());
        } catch (Exception e) {
            System.err.println("Error al enviar a Kafka: " + e.getMessage());
        }
    }

}
