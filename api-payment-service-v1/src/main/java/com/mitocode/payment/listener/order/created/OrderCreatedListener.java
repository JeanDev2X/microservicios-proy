package com.mitocode.payment.listener.order.created;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitocode.payment.listener.order.created.event.OrderCreatedEvent;
import com.mitocode.payment.service.PaymentService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedListener {

    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;

    public OrderCreatedListener(PaymentService paymentService, ObjectMapper objectMapper) {
        this.paymentService = paymentService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "order-created-topic", groupId = "payment-group")
    public void onMessage(String message) {
        try {
            // 1. Convertir el JSON de Kafka al objeto Java
            OrderCreatedEvent event = objectMapper.readValue(message, OrderCreatedEvent.class);
            System.out.println("Orden recibida para pago: " + event.getOrderId());

            // 2. Llamar al servicio para que haga la magia (Guardar en DB y llamar a Visa)
            paymentService.processOrderPayment(event);

        } catch (Exception e) {
            System.err.println("Error procesando orden en Payment: " + e.getMessage());
        }
    }

}
