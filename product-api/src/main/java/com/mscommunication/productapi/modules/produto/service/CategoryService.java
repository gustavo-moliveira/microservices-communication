package com.mscommunication.productapi.modules.produto.service;

import com.mscommunication.productapi.config.exception.ValidationException;
import com.mscommunication.productapi.modules.produto.dto.CategoryRequest;
import com.mscommunication.productapi.modules.produto.dto.CategoryResponse;
import com.mscommunication.productapi.modules.produto.model.Category;
import com.mscommunication.productapi.modules.produto.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;


@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

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