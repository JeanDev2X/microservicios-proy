package com.mitocode.inventory.service;
import com.mitocode.inventory.infraestructure.document.ProductDocument;
import com.mitocode.inventory.infraestructure.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Para el Pre-check (Síncrono)
    public boolean validateStock(String productId, Integer quantity) {
        return productRepository.findByProductId(productId)
                .map(product -> product.getStock() >= quantity)
                .orElse(false);
    }

    // Para el descuento final (Asíncrono tras pago)
    public void updateStock(String productId, Integer quantity) {
        ProductDocument product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // --- NUEVA RESTRICCIÓN ---
        if (product.getStock() < quantity) {
            throw new RuntimeException("Stock insuficiente. Disponible: "
                    + product.getStock() + ", Solicitado: " + quantity);
        }
        // -------------------------

        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }

    public void restoreStock(String productId, int quantity) {
        ProductDocument product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Sumamos el stock que se había restado previamente
        product.setStock(product.getStock() + quantity);
        productRepository.save(product);
    }

}
