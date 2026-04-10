package com.mitocode.inventory.listener.payment.failed;

import com.mitocode.inventory.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentFailedListener {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "payment-failed-topic", groupId = "inventory-group")
    public void onPaymentFailed(String message) {
        try {
            // Suponiendo que el evento trae productId y quantity
            // Si el evento de falla solo trae orderId, necesitarías una tabla de "Reservas"
            // Pero para efectos del proyecto, solemos re-usar el evento original de pago
            var event = objectMapper.readTree(message);
            String productId = event.get("productId").asText();
            int quantity = event.get("quantity").asInt();

            System.out.println("COMPENSACIÓN: Devolviendo " + quantity + " unidades al producto " + productId);

            // Llamamos al servicio para SUMAR el stock
            productService.restoreStock(productId, quantity);

        } catch (Exception e) {
            System.err.println("Error en compensación de inventario: " + e.getMessage());
        }
    }

}
