package com.mitocode.inventory.listener.payment.completed;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitocode.inventory.listener.payment.completed.event.PaymentCompletedEvent;
import com.mitocode.inventory.producer.stock.failed.StockOperationFailedProducer;
import com.mitocode.inventory.producer.stock.failed.event.StockOperationFailedEvent;
import com.mitocode.inventory.producer.stock.reserved.StockReservedProducer;
import com.mitocode.inventory.producer.stock.reserved.event.StockReservedEvent;
import com.mitocode.inventory.service.ProductService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentCompletedListener {

    private final ProductService productService;
    private final ObjectMapper objectMapper; // El traductor de JSON <-> String
    private final StockReservedProducer stockReservedProducer; // Inyectamos el nuevo productor
    private final StockOperationFailedProducer stockOperationFailedProducer; // Inyectar nuevo

    public PaymentCompletedListener(ProductService productService
                                    ,ObjectMapper objectMapper
                                    ,StockReservedProducer stockReservedProducer
                                    ,StockOperationFailedProducer stockOperationFailedProducer) {
        this.productService = productService;
        this.objectMapper = objectMapper;
        this.stockReservedProducer = stockReservedProducer;
        this.stockOperationFailedProducer = stockOperationFailedProducer;
    }

    @KafkaListener(topics = "payment-completed-topic", groupId = "inventory-group")
    public void onMessage(String message) {

        System.out.println("LLEGÓ ALGO: " + message);
        PaymentCompletedEvent event = null;
        try {
            // 1. Convertimos el String manual a nuestro Objeto
            event = objectMapper.readValue(message, PaymentCompletedEvent.class);
            System.out.println("Inventario recibió pago de orden: " + event.getOrderId());

            // 2. Ejecutamos la lógica de negocio (Restar stock en Mongo)
            productService.updateStock(event.getProductId(), event.getQuantity());
            System.out.println("Inventario actualizado para orden: " + event.getOrderId());

            // 3. TODO: Aquí iría el Producer para avisar que el stock ya se reservó
            StockReservedEvent response = new StockReservedEvent(event.getOrderId(), "RESERVED");
            stockReservedProducer.send(response);

        } catch (Exception e) {
            System.err.println("Error procesando mensaje de Kafka: " + e.getMessage());

            // 3. Si hubo error (ej. Stock insuficiente), avisamos FALLA
            if (event != null) {
                StockOperationFailedEvent failEvent = new StockOperationFailedEvent(
                        event.getOrderId(),
                        e.getMessage(),
                        "FAILED"
                );
                stockOperationFailedProducer.send(failEvent);
            }
        }
    }

}
