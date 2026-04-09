package com.mitocode.payment.listener.inventory.failed.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockOperationFailedEvent {
    private String orderId;
    private String productId;
    private Integer quantity;
    private String reason; // Ejemplo: "INSUFFICIENT_STOCK"
}