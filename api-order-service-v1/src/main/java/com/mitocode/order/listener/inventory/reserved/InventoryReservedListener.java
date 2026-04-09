package com.mitocode.order.listener.inventory.reserved;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitocode.order.listener.inventory.reserved.event.InventoryReservedEvent;
import com.mitocode.order.infraestructure.repository.OrderRepository;
import com.mitocode.order.infraestructure.entity.OrderEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryReservedListener {
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    public InventoryReservedListener(OrderRepository orderRepository, ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "stock-reserved-topic", groupId = "order-group")
    public void onInventoryReserved(String message) {
        try {
            InventoryReservedEvent event = objectMapper.readValue(message, InventoryReservedEvent.class);

            // Buscamos la orden original
            orderRepository.findByOrderId(event.getOrderId()).ifPresent(order -> {
                order.setStatus("CONFIRMED"); // <--- ¡AQUÍ CAMBIA EL ESTADO!
                orderRepository.save(order);
                System.out.println("[OrderService] Orden CONFIRMADA: " + event.getOrderId());
            });

        } catch (Exception e) {
            System.err.println("Error al confirmar la orden: " + e.getMessage());
        }
    }
}
