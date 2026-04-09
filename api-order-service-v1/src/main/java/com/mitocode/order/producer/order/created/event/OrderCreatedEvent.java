package com.mitocode.order.producer.order.created.event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCreatedEvent {
    private String orderId;
    private String productId;
    private Integer quantity;
    private Double amount;
}
