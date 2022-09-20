package com.mscommunication.productapi.modules.product.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mscommunication.productapi.config.exception.SuccessResponse;
import com.mscommunication.productapi.config.exception.ValidationException;
import com.mscommunication.productapi.modules.product.category.service.CategoryService;
import com.mscommunication.productapi.modules.product.product.dto.*;
import com.mscommunication.productapi.modules.product.product.model.Product;
import com.mscommunication.productapi.modules.product.product.repository.ProductRepository;
import com.mscommunication.productapi.modules.product.sales.client.SalesClient;
import com.mscommunication.productapi.modules.product.sales.dto.SalesConfirmationDTO;
import com.mscommunication.productapi.modules.product.sales.dto.SalesProductResponse;
import com.mscommunication.productapi.modules.product.sales.enums.SalesStatus;
import com.mscommunication.productapi.modules.product.sales.rabbitmq.SalesConfirmationSender;
import com.mscommunication.productapi.modules.product.supplier.service.SupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mscommunication.productapi.config.RequestUtil.getCurrentRequest;
import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
public class ProductService {
    private static final Integer ZERO = 0;
    private static final String TRANSACTION_ID = "transactionid";
    private static final String SERVICE_ID = "serviceid";

    @Autowired
    private ProductRepository productRepository;
    @Lazy
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SalesConfirmationSender salesConfirmationSender;
    @Autowired
    private SalesClient salesClient;

    public ProductResponse save(ProductRequest request) {
        validateProductDataInformed(request);
        validateCategoryAndSupllierIdInformed(request);
        var category = categoryService.findById(request.getCategoryId());
        var supplier = supplierService.findById(request.getSupplierId());
        var product = productRepository.save(Product.of(request, category, supplier));
        return ProductResponse.of(product);
    }

    public ProductResponse update(ProductRequest request,
                                  Integer id) {
        validateProductDataInformed(request);
        validateInformedId(id);
        validateCategoryAndSupllierIdInformed(request);
        var category = categoryService.findById(request.getCategoryId());
        var supplier = supplierService.findById(request.getSupplierId());
        var product = Product.of(request, category, supplier);
        product.setId(id);
        productRepository.save(product);
        return ProductResponse.of(product);
    }

    public Product findById(Integer id) {
        validateInformedId(id);
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException("There's no product for the given ID."));
    }
    public ProductResponse findByIdResponse(Integer id) {
        return ProductResponse.of(findById(id));
    }

    public List<ProductResponse> findByName(String name) {
        if (isEmpty(name)) {
            throw new ValidationException("The product name must be informed.");
        }
        return productRepository
                .findByNameIgnoreCaseContaining(name)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> findAll() {
        return productRepository
                .findAll()
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> findBySupplierId(Integer supplierId) {
        if (isEmpty(supplierId)) {
            throw new ValidationException("The product's supplier ID must be informed.");
        }
        return productRepository
                .findBySupplierId(supplierId)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> findByCategoryId(Integer categoryId) {
        if (isEmpty(categoryId)) {
            throw new ValidationException("The product's category ID must be informed.");
        }
        return productRepository
                .findByCategoryId(categoryId)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public Boolean existsByCategoryId(Integer categoryId) {
        return productRepository.existsByCategoryId(categoryId);
    }
    public Boolean existsBySupplierId(Integer supplierId) {
        return productRepository.existsBySupplierId(supplierId);
    }

    public SuccessResponse delete(Integer id) {
        validateInformedId(id);
        productRepository.deleteById(id);
        return SuccessResponse.create("The product was deleted.");
    }

    private void validateInformedId(Integer id) {
        if (isEmpty(id)) {
            throw new ValidationException("The supplier ID must be informed.");
        }
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

    public void updateProductStock(ProductStockDTO product) {
        try {
            validateStockUpdateData(product);
            updateStock(product);
        } catch (Exception e) {
            log.error("Error while trying to update stock for message with error {}", e.getMessage(), e);
            var rejectedMessage = new SalesConfirmationDTO(product.getSalesId(), SalesStatus.REJECTED, product.getTransactionid());
            salesConfirmationSender.sendSalesConfirmationMessage(rejectedMessage);
        }
    }

    private void updateStock(ProductStockDTO product) {
        var productsForUpdate = new ArrayList<Product>();
        product
            .getProducts()
            .forEach(salesProduct -> {
                var existingProduct = findById(salesProduct.getProductId());
                validateQuantityInStock(salesProduct, existingProduct);
                existingProduct.updateStock(salesProduct.getQuantity());
                productsForUpdate.add(existingProduct);
                productRepository.save(existingProduct);
            });
        if (!isEmpty(productsForUpdate)) {
            productRepository.saveAll(productsForUpdate);
            var approvedMessage = new SalesConfirmationDTO(product.getSalesId(), SalesStatus.APPROVED, product.getTransactionid());
            salesConfirmationSender.sendSalesConfirmationMessage(approvedMessage);
        }
    }

    private void validateQuantityInStock(ProductQuantityDTO salesProduct,
                                         Product existingProduct) {
        if (salesProduct.getQuantity() > existingProduct.getQuantityAvailable()) {
            throw new ValidationException(
                    String.format("The product %s is out of stock.", existingProduct.getId()));
        }
    }

    @Transactional
    void validateStockUpdateData(ProductStockDTO product) {
        if (isEmpty(product)
        || isEmpty(product.getSalesId())) {
            throw new ValidationException("The product data and the sales ID must be informed.");
        }
        if (isEmpty(product.getProducts())) {
            throw new ValidationException("The sales' products must be informed.");
        }
        product
            .getProducts()
            .forEach(salesProduct -> {
                if (isEmpty(salesProduct.getQuantity())
                || isEmpty(salesProduct.getProductId())) {
                    throw new ValidationException("The productID and the quantity must be informed.");
                }
            });
    }

    public ProductSalesResponse findProductSales(Integer id) {
        var product = findById(id);
        var sales = getSalesByProductId(product.getId());
        return ProductSalesResponse.of(product, sales.getSalesIds());
    }

    private SalesProductResponse getSalesByProductId(Integer productId) {
        try {
            var currentRequest = getCurrentRequest();
            var transactionid = currentRequest.getHeader(TRANSACTION_ID);
            var serviceid = currentRequest.getAttribute(SERVICE_ID);
            log.info("Sending GET request to orders by productId with data {} | transactionID: {} | serviceID: {}",
                    productId, transactionid, serviceid);
            var response = salesClient
                    .findSalesByProductId(productId)
                    .orElseThrow(() -> new ValidationException("The sales was not found by this product."));
            log.info("Receiving response from orders by productId with data {} | transactionID: {} | serviceID: {}",
                    new ObjectMapper().writeValueAsString(response), transactionid, serviceid);
            return response;
        } catch (Exception e) {
            throw new ValidationException("There was an error trying to get the product's sales.");
        }
    }

    public SuccessResponse checkProductsStock(ProductCheckStockRequest request) {
        try {
            var currentRequest = getCurrentRequest();
            var transactionid = currentRequest.getHeader(TRANSACTION_ID);
            var serviceid = currentRequest.getAttribute(SERVICE_ID);
            log.info("Request to POST product stock with data {} | transactionID: {} | serviceID: {}",
                    new ObjectMapper().writeValueAsString(request), transactionid, serviceid);
            if (isEmpty(request)
                || isEmpty(request.getProducts())) {
                throw new ValidationException("The request data and products must be informed.");
            }
            request
                .getProducts()
                .forEach(this::validateStock);
            var response = SuccessResponse.create("The stock is ok!");
            log.info("Response to POST product stock with data {} | transactionID: {} | serviceID: {}",
                    new ObjectMapper().writeValueAsString(response), transactionid, serviceid);
            return response;
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
    }

    private void validateStock(ProductQuantityDTO productQuantity) {
        if (isEmpty(productQuantity.getProductId())
        || isEmpty(productQuantity.getQuantity())) {
            throw new ValidationException("Product ID and quantity must be informed.");
        }
        var product = findById(productQuantity.getProductId());
        if (productQuantity.getQuantity() > product.getQuantityAvailable()) {
            throw new ValidationException(String.format("The product %s is out of stock", product.getId()));
        }
    }
}