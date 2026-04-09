package com.mitocode.inventory.infraestructure.repository;

import com.mitocode.inventory.infraestructure.document.ProductDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<ProductDocument, String> {
    //Método para buscar por nuestro código de producto
    Optional<ProductDocument> findByProductId(String productId);
}
