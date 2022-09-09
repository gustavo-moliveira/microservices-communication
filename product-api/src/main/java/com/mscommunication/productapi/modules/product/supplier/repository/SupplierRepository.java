package com.mscommunication.productapi.modules.product.supplier.repository;

import com.mscommunication.productapi.modules.product.supplier.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    List<Supplier> findByNameIgnoreCaseContaining(String description);
}