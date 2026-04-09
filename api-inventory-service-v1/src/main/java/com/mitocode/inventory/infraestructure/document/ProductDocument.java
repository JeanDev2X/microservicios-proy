package com.mitocode.inventory.infraestructure.document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "products")
public class ProductDocument {

    @Id
    private String id;          // ID autogenerado por Mongo (String)
    private String productId;   // Nuestro código de negocio (ej: PROD-001)
    private String name;
    private BigDecimal price;
    private Integer stock;

}
