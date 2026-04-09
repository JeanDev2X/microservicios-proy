package com.mitocode.inventory.infraestructure.repository;

import com.mitocode.inventory.infraestructure.document.ProductDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataSetup implements CommandLineRunner {

    @Autowired
    private ProductRepository repo;

    @Override
    public void run(String... args) throws Exception {
        if(repo.count() == 0) {
            repo.save(new ProductDocument(null, "P001", "Laptop", new BigDecimal("1000"), 10));
            repo.save(new ProductDocument(null, "P002", "Mouse", new BigDecimal("20"), 50));
            System.out.println("¡Datos de MongoDB inicializados!");
        }
    }

}
