package com.mitocode.inventory.listener.order.created.event;

import lombok.Data;

@Data
public class OrderCreatedEvent {
    private String orderId;
    private Double amount;

}
