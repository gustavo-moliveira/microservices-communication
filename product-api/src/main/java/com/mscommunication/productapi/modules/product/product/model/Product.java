package com.mscommunication.productapi.modules.product.product.model;

import com.mscommunication.productapi.modules.product.category.model.Category;
import com.mscommunication.productapi.modules.product.product.dto.ProductRequest;
import com.mscommunication.productapi.modules.product.supplier.model.Supplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "fk_supplier", nullable = false)
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "fk_category", nullable = false)
    private Category category;

    @Column(name = "quantity_available", nullable = false)
    private Integer quantityAvailable;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public static Product of(ProductRequest request,
                             Category category,
                             Supplier supplier) {
        return Product.builder()
                .name(request.getName())
                .quantityAvailable(request.getQuantityAvailable())
                .supplier(supplier)
                .category(category)
                .build();
    }
}
