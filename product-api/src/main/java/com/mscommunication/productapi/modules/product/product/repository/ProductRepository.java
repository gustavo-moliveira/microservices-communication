package com.mscommunication.productapi.modules.product.product.repository;

import com.mscommunication.productapi.modules.product.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByNameIgnoreCaseContaining(String description);
    List<Product> findByCategoryId(Integer id);
    List<Product> findBySupplierId(Integer id);
    Boolean existsByCategoryId(Integer id);
    Boolean existsBySupplierId(Integer id);
}
