package com.mitocode.inventory.producer.stock.failed.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockOperationFailedEvent {
    private String orderId;
    private String reason;
    private String status; // Ej: "FAILED"
}
