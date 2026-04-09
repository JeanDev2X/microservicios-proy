package com.mitocode.order.listener.payment.failed;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitocode.order.infraestructure.repository.OrderRepository;
import com.mitocode.order.listener.payment.failed.event.PaymentFailedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentFailedListener {
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    public PaymentFailedListener(OrderRepository orderRepository, ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "payment-failed-topic", groupId = "order-group")
    public void onPaymentFailed(String message) {
        try {
            PaymentFailedEvent event = objectMapper.readValue(message, PaymentFailedEvent.class);
            orderRepository.findByOrderId(event.getOrderId()).ifPresent(order -> {
                order.setStatus("FAILED"); // O "CANCELLED"
                orderRepository.save(order);
                System.err.println("[OrderService] Orden marcada como FAILED por pago rechazado.");
            });
        } catch (Exception e) {
            System.err.println("[OrderService] Error al procesar evento de pago fallido: " + e.getMessage());
        }
    }
}
