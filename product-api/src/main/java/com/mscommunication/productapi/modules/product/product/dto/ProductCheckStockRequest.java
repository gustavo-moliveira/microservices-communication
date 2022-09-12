package com.mscommunication.productapi.modules.product.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCheckStockRequest {
    List<ProductQuantityDTO> products;
}
