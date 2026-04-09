package com.mitocode.order.controller.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private String productId;
    private Integer quantity;
    private Double amount;
}
