package com.mitocode.payment.producer.payment.completed.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentCompletedEvent {
    private String orderId;
    private String productId; // Esto vendrá de la orden original
    private Integer quantity;  // Esto vendrá de la orden original
    private String status;     // "COMPLETED"
}
