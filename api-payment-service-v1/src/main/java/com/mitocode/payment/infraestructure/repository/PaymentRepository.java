package com.mitocode.payment.infraestructure.repository;
import com.mitocode.payment.infraestructure.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long>{
    // Necesitamos buscar por orderId para saber a quién devolverle el dinero
    java.util.Optional<PaymentEntity> findByOrderId(String orderId);
}
