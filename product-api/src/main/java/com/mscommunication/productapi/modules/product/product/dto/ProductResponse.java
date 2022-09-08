package com.mscommunication.productapi.modules.product.product.dto;

import com.mscommunication.productapi.modules.product.category.model.Category;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class ProductResponse {
    private Integer id;
    private String description;

    public static ProductResponse of(Category category) {
        var response = new ProductResponse();
        BeanUtils.copyProperties(category, response);
        return response;
    }
}
