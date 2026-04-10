package com.mitocode.payment.listener.inventory.failed;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitocode.payment.listener.inventory.failed.event.StockOperationFailedEvent;
import com.mitocode.payment.service.PaymentService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class StockFailedListener {

    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;

    public StockFailedListener(PaymentService paymentService, ObjectMapper objectMapper) {
        this.paymentService = paymentService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "stock-failed-topic", groupId = "payment-group")
    public void onStockFailed(String message) {
        try {

            StockOperationFailedEvent event = objectMapper.readValue(message, StockOperationFailedEvent.class);

            System.err.println("Reintentando compensación para Orden: " + event.getOrderId() +
                    " Motivo: " + event.getReason());

            // Ejecutamos el reembolso
            paymentService.processRefund(event.getOrderId());

        } catch (Exception e) {
            System.err.println("Error al procesar el rollback de pago: " + e.getMessage());
        }
    }

}
