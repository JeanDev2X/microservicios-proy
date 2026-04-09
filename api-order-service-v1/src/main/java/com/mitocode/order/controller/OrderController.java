package com.mitocode.order.controller;

import com.mitocode.order.controller.dto.OrderRequest;
import com.mitocode.order.infraestructure.entity.OrderEntity;
import com.mitocode.order.service.saga.OrderSagaService; // <--- Importación corregida
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID; // <--- No olvides este import

@RestController
@RequestMapping("/orders")
public class OrderController {
    // Cambiamos OrderService por OrderSagaService para que coincida con tu estructura
    private final OrderSagaService orderSagaService;

    public OrderController(OrderSagaService orderSagaService) {
        this.orderSagaService = orderSagaService;
    }

    @PostMapping
    public ResponseEntity<OrderEntity> createOrder(@RequestBody OrderRequest request) {
        // Generamos el ID único
        String generatedOrderId = UUID.randomUUID().toString();

        System.out.println("[OrderController] Recibida petición para OrderId: " + generatedOrderId);

        // Llamamos al método que definimos en el SagaService
        OrderEntity order = orderSagaService.createOrder(request, generatedOrderId);

        return ResponseEntity.ok(order);
    }
}
