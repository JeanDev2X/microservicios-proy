package com.mitocode.order.listener.inventory.reserved.event;

import lombok.Data;

@Data
public class InventoryReservedEvent {
    private String orderId;
    private String status; // "RESERVED"
}
