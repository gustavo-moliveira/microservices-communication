package com.mscommunication.productapi.modules.produto.repository;

import com.mscommunication.productapi.modules.produto.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
