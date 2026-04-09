package com.mitocode.inventory.listener.payment.completed.event;

import lombok.Data;

@Data
public class PaymentCompletedEvent {
    private String orderId;
    private String productId;
    private Integer quantity;
    private String status;
}