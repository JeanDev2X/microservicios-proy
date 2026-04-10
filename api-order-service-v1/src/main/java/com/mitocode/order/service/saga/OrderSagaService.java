package com.mitocode.order.service.saga;
import com.mitocode.order.controller.dto.OrderRequest;
import com.mitocode.order.infraestructure.entity.OrderEntity;
import com.mitocode.order.infraestructure.repository.OrderRepository;
import com.mitocode.order.producer.order.created.OrderCreatedProducer;
import com.mitocode.order.producer.order.created.event.OrderCreatedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderSagaService {
    private final OrderRepository orderRepository;
    private final OrderCreatedProducer orderCreatedProducer;

    public OrderSagaService(OrderRepository orderRepository, OrderCreatedProducer orderCreatedProducer) {
        this.orderRepository = orderRepository;
        this.orderCreatedProducer = orderCreatedProducer;
    }

    @Transactional
    public OrderEntity createOrder(OrderRequest request, String orderId) {
        // 1. Guardar en Postgres (Estado inicial PENDING)
        OrderEntity order = new OrderEntity();
        order.setOrderId(orderId);
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());
        order.setAmount(request.getAmount());
        order.setStatus("PENDING");

        OrderEntity savedOrder = orderRepository.save(order);

        // 2. Notificar a Payment via Kafka
        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(orderId)
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .amount(request.getAmount())
                .build();

        orderCreatedProducer.send(event);

        return savedOrder;
    }

    @Transactional(readOnly = true)
    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<OrderEntity> getOrderById(String orderId) {
        // Buscamos por el orderId (UUID) que es el que manejas en la Saga
        return orderRepository.findByOrderId(orderId);
    }

}
