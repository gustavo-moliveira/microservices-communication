package com.mscommunication.productapi.modules.product.category.repository;

import com.mscommunication.productapi.modules.product.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByDescriptionIgnoreCaseContaining(String description);
}
