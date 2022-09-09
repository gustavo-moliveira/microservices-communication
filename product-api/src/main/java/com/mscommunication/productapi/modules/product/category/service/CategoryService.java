package com.mscommunication.productapi.modules.product.category.service;

import com.mscommunication.productapi.config.exception.ValidationException;
import com.mscommunication.productapi.modules.product.category.dto.CategoryRequest;
import com.mscommunication.productapi.modules.product.category.dto.CategoryResponse;
import com.mscommunication.productapi.modules.product.category.model.Category;
import com.mscommunication.productapi.modules.product.category.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;


@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category findById(Integer id) {
        if (isEmpty(id)) {
            throw new ValidationException("The category ID was not informed.");
        }
        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException("There's no category for the given ID."));
    }

      public CategoryResponse findByIdResponse(Integer id) {
        return CategoryResponse.of(findById(id));
    }

    public List<CategoryResponse> findByDescriptionIgnoreCaseContaining(String description) {
        if (isEmpty(description)) {
            throw new ValidationException("The category description must be informed.");
        }
        return categoryRepository
                .findByDescriptionIgnoreCaseContaining(description)
                .stream()
                .map(CategoryResponse::of)
                .collect(Collectors.toList());
    }

    public List<CategoryResponse> findAll() {
        return categoryRepository
                .findAll()
                .stream()
                .map(CategoryResponse::of)
                .collect(Collectors.toList());
    }

    public CategoryResponse save(CategoryRequest request) {
        validateCategoryNameInformed(request);
        var category = categoryRepository.save(Category.of(request));
        return CategoryResponse.of(category);
    }

    private void validateCategoryNameInformed(CategoryRequest request) {
        if (isEmpty(request.getDescription())) {
            throw new ValidationException("The category description was not informed.");
        }
    }
}