package com.mitocode.payment.service;

import com.mitocode.payment.client.dto.VisaRequest;
import com.mitocode.payment.client.dto.VisaResponse;
import com.mitocode.payment.client.restclient.VisaRestClient;
import com.mitocode.payment.infraestructure.entity.PaymentEntity;
import com.mitocode.payment.infraestructure.repository.PaymentRepository;
import com.mitocode.payment.listener.order.created.event.OrderCreatedEvent;
import com.mitocode.payment.producer.payment.completed.PaymentCompletedProducer;
import com.mitocode.payment.producer.payment.completed.event.PaymentCompletedEvent;
import com.mitocode.payment.producer.payment.failed.PaymentFailedProducer;
import com.mitocode.payment.producer.payment.failed.event.PaymentFailedEvent;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final VisaRestClient visaRestClient;
    private final PaymentCompletedProducer paymentCompletedProducer;
    private final PaymentFailedProducer paymentFailedProducer;

    public PaymentService(PaymentRepository paymentRepository, VisaRestClient visaRestClient, PaymentCompletedProducer paymentCompletedProducer, PaymentFailedProducer paymentFailedProducer) {
        this.paymentRepository = paymentRepository;
        this.visaRestClient = visaRestClient;
        this.paymentCompletedProducer = paymentCompletedProducer;
        this.paymentFailedProducer = paymentFailedProducer;
    }

    public void processOrderPayment(OrderCreatedEvent event) {
        // 1. PRIMERO validamos si ya existe. Si existe, nos vamos.
        if (paymentRepository.findByOrderId(event.getOrderId()).isPresent()) {
            System.out.println("La orden ya fue procesada. Ignorando duplicado" + event.getOrderId());
            return;
        }

        // A. Guardar registro inicial en Postgres (PENDING)
        PaymentEntity payment = new PaymentEntity();
        payment.setOrderId(event.getOrderId());
        payment.setAmount(event.getAmount());
        payment.setStatus("PENDING");
        paymentRepository.save(payment);

        // Adaptamos al formato de Visa: accountId y BigDecimal
        VisaRequest visaRequest = new VisaRequest(
                "ACC-123", // Una cuenta ficticia que Visa acepte
                java.math.BigDecimal.valueOf(event.getAmount())
        );

        try {
            VisaResponse visaResponse = visaRestClient.processPayment(visaRequest);

            if ("SUCCESS".equals(visaResponse.status())) {
                // --- CASO 1: ÉXITO TOTAL ---
                payment.setStatus("COMPLETED");
                payment.setTransactionId(visaResponse.accountId());
                paymentRepository.save(payment);
                System.out.println("Pago aprobado por Visa para orden: " + event.getOrderId());

                // Notificamos al INVENTARIO para que descuente stock
                PaymentCompletedEvent completedEvent = PaymentCompletedEvent.builder()
                        .orderId(event.getOrderId())
                        .productId(event.getProductId())
                        .quantity(event.getQuantity())
                        .status("COMPLETED")
                        .build();
                paymentCompletedProducer.send(completedEvent);
            } else {
                // --- CASO 2: FALLO (VISA RECHAZÓ O CIRCUIT BREAKER ACTIVADO) ---
                payment.setStatus("FAILED");
                paymentRepository.save(payment);
                System.err.println("Pago fallido en Visa/CircuitBreaker. Status: " + visaResponse.status());

                // IMPORTANTE: Notificar al ORDER SERVICE para que cancele la orden
                // Aquí deberías tener un Producer que envíe a "payment-failed-topic"
                // paymentFailedProducer.send(new PaymentFailedEvent(event.getOrderId(), "PAYMENT_REJECTED"));
                // 1. Creamos el evento de falla
                PaymentFailedEvent failedEvent = new PaymentFailedEvent();
                failedEvent.setOrderId(event.getOrderId());
                failedEvent.setReason("PAYMENT_REJECTED_OR_CIRCUIT_BREAKER_ACTIVE");

                // 2. Notificamos al Order Service para que pase de PENDING a FAILED
                paymentFailedProducer.send(failedEvent);
            }

        } catch (Exception e) {
            // --- CASO 3: ERROR TÉCNICO INESPERADO ---
            payment.setStatus("FAILED");
            paymentRepository.save(payment);

            PaymentFailedEvent errorEvent = new PaymentFailedEvent();
            errorEvent.setOrderId(event.getOrderId());
            errorEvent.setReason("TECHNICAL_ERROR: " + e.getMessage());

            paymentFailedProducer.send(errorEvent);
            System.err.println("Error crítico: " + e.getMessage());
        }
    }

    public void processRefund(String orderId) {
        // 1. Buscar el pago en la BD
        PaymentEntity payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado para reembolso"));

        // 2. Lógica de negocio: Solo reembolsar si estaba COMPLETED
        if ("COMPLETED".equals(payment.getStatus())) {
            try {
                // Aquí llamarías a Visa al endpoint de refund (si lo tuviera)
                // Por ahora, simulamos el éxito del reembolso

                payment.setStatus("REFUNDED");
                paymentRepository.save(payment);

                System.out.println("Reembolso completado con éxito para la orden: " + orderId);

            } catch (Exception e) {
                System.err.println("Falló el reembolso técnico: " + e.getMessage());
            }
        }
    }
}
