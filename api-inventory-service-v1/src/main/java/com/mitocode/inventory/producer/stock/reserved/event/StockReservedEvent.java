package com.mitocode.inventory.producer.stock.reserved.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockReservedEvent {

    private String orderId;
    private String status; // Ej: "RESERVED"

}
