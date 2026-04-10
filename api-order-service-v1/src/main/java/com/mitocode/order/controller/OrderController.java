package com.mitocode.order.controller;

import com.mitocode.order.controller.dto.OrderRequest;
import com.mitocode.order.infraestructure.entity.OrderEntity;
import com.mitocode.order.service.saga.OrderSagaService; // <--- Importación corregida
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID; // <--- No olvides este import

@RestController
@RequestMapping("/orders")
public class OrderController {
    // Cambiamos OrderService por OrderSagaService para que coincida con tu estructura
    private final OrderSagaService orderSagaService;

    public OrderController(OrderSagaService orderSagaService) {
        this.orderSagaService = orderSagaService;
    }

    @Operation(summary = "Crear nueva orden", description = "Inicia el flujo SAGA para la creación de una orden")
    @PostMapping
    public ResponseEntity<OrderEntity> createOrder(@RequestBody OrderRequest request) {
        // Generamos el ID único
        String generatedOrderId = UUID.randomUUID().toString();

        System.out.println("[OrderController] Recibida petición para OrderId: " + generatedOrderId);

        // Llamamos al método que definimos en el SagaService
        OrderEntity order = orderSagaService.createOrder(request, generatedOrderId);

        return ResponseEntity.ok(order);
    }

    // Endpoint para listar todas las órdenes
    @Operation(summary = "Obtener lista de órdenes", description = "Retorna todas las órdenes almacenadas en Postgres")
    @GetMapping
    public ResponseEntity<List<OrderEntity>> getAllOrders() {
        List<OrderEntity> orders = orderSagaService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderEntity> getOrder(@PathVariable String orderId) {
        return orderSagaService.getOrderById(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
