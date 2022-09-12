package com.mscommunication.productapi.modules.product.sales.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesProductResponse {
    private List<String> salesIds;
}
