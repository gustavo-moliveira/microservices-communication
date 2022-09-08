package com.mscommunication.productapi.modules.produto.repository;

import com.mscommunication.productapi.modules.produto.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
