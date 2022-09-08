package com.mscommunication.productapi.modules.product.supplier.repository;

import com.mscommunication.productapi.modules.product.supplier.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
}
