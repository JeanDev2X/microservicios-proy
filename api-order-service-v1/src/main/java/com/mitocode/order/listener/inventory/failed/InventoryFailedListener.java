package com.mitocode.order.listener.inventory.failed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitocode.order.listener.inventory.failed.event.StockOperationFailedEvent;
import com.mitocode.order.infraestructure.repository.OrderRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryFailedListener {
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    public InventoryFailedListener(OrderRepository orderRepository, ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "stock-failed-topic", groupId = "order-group")
    public void onInventoryFailed(String message) {
        try {
            StockOperationFailedEvent event = objectMapper.readValue(message, StockOperationFailedEvent.class);

            System.err.println("[OrderService] Cancelando orden por falta de stock: " + event.getOrderId());

            orderRepository.findByOrderId(event.getOrderId()).ifPresent(order -> {
                order.setStatus("CANCELLED"); // <--- Marcamos como cancelada
                orderRepository.save(order);
            });

        } catch (Exception e) {
            System.err.println("Error al procesar falla de inventario en Order: " + e.getMessage());
        }
    }
}
