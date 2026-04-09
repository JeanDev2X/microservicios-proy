package com.mitocode.order.infraestructure.repository;

import com.mitocode.order.infraestructure.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long>{
    // Para buscar la orden cuando el Inventario nos avise que todo salió bien
    Optional<OrderEntity> findByOrderId(String orderId);
}
