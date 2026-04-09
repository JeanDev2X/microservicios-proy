package com.mitocode.order.listener.inventory.failed.event;
import lombok.Data;

@Data
public class StockOperationFailedEvent {
    private String orderId;
    private String productId;
    private Integer quantity;
    private String reason; // Ej: "INSUFFICIENT_STOCK"
}
