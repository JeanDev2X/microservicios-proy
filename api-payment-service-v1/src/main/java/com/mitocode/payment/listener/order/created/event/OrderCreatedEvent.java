package com.mitocode.payment.listener.order.created.event;
import lombok.Data;

@Data
public class OrderCreatedEvent {
    private String orderId;
    private Double amount;
    private String productId; // <--- Agregado
    private Integer quantity; // <--- Agregado
}
