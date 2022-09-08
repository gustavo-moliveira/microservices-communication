package com.mscommunication.productapi.modules.product.supplier.service;

import com.mscommunication.productapi.config.exception.ValidationException;
import com.mscommunication.productapi.modules.product.category.dto.CategoryRequest;
import com.mscommunication.productapi.modules.product.category.dto.CategoryResponse;
import com.mscommunication.productapi.modules.product.category.model.Category;
import com.mscommunication.productapi.modules.product.category.repository.CategoryRepository;
import com.mscommunication.productapi.modules.product.supplier.dto.SupplierRequest;
import com.mscommunication.productapi.modules.product.supplier.dto.SupplierResponse;
import com.mscommunication.productapi.modules.product.supplier.model.Supplier;
import com.mscommunication.productapi.modules.product.supplier.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;


@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;

    public SupplierResponse save(SupplierRequest request) {
        validateSupplierNameInformed(request);
        var supplier = supplierRepository.save(Supplier.of(request));
        return SupplierResponse.of(supplier);
    }

    private void validateSupplierNameInformed(SupplierRequest request) {
        if (isEmpty(request.getName())) {
            throw new ValidationException("The supplier name was not informed.");
        }
    }
}