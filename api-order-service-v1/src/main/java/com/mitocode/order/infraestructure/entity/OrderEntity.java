package com.mitocode.order.infraestructure.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId; // El UUID que usaremos en todo el flujo
    private String productId;
    private Integer quantity;
    private Double amount;
    private String status; // PENDING, CONFIRMED, CANCELLED, FAILED, PAYMENT_RECEIVED
    private LocalDateTime createdAt = LocalDateTime.now();

}
