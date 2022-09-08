package com.mscommunication.productapi.modules.produto.repository;

import com.mscommunication.productapi.modules.produto.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
}
