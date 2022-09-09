package com.mscommunication.productapi.modules.product.product.service;

import com.mscommunication.productapi.config.exception.ValidationException;
import com.mscommunication.productapi.modules.product.category.dto.CategoryRequest;
import com.mscommunication.productapi.modules.product.category.dto.CategoryResponse;
import com.mscommunication.productapi.modules.product.category.model.Category;
import com.mscommunication.productapi.modules.product.category.repository.CategoryRepository;
import com.mscommunication.productapi.modules.product.category.service.CategoryService;
import com.mscommunication.productapi.modules.product.product.dto.ProductRequest;
import com.mscommunication.productapi.modules.product.product.dto.ProductResponse;
import com.mscommunication.productapi.modules.product.product.model.Product;
import com.mscommunication.productapi.modules.product.product.repository.ProductRepository;
import com.mscommunication.productapi.modules.product.supplier.model.Supplier;
import com.mscommunication.productapi.modules.product.supplier.repository.SupplierRepository;
import com.mscommunication.productapi.modules.product.supplier.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;


@Service
public class ProductService {
    private static final Integer ZERO = 0;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private CategoryService categoryService;

    public ProductResponse save(ProductRequest request) {
        validateProductDataInformed(request);
        validateCategoryAndSupllierIdInformed(request);
        var category = categoryService.findById(request.getCategoryId());
        var supplier = supplierService.findById(request.getSupplierId());
        var product = productRepository.save(Product.of(request, category, supplier));
        return ProductResponse.of(product);
    }

    private void validateProductDataInformed(ProductRequest request) {
        if (isEmpty(request.getName())) {
            throw new ValidationException("The product's name was not informed.");
        }
        if (isEmpty(request.getQuantityAvailable())) {
            throw new ValidationException("The product's quantity was not informed.");
        }
        if (request.getQuantityAvailable() <= ZERO) {
            throw new ValidationException("The quantity should not be less or equal to zero.");
        }
    }

    private void validateCategoryAndSupllierIdInformed (ProductRequest request) {
        if (isEmpty(request.getCategoryId())) {
            throw new ValidationException("The category ID was not informed.");
        }
        if (isEmpty(request.getSupplierId())) {
            throw new ValidationException("The supplier ID was not informed.");
        }
    }
}