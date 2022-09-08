package com.mscommunication.productapi.modules.produto.controller;

import com.mscommunication.productapi.modules.produto.dto.CategoryRequest;
import com.mscommunication.productapi.modules.produto.dto.CategoryResponse;
import com.mscommunication.productapi.modules.produto.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public CategoryResponse save(@RequestBody CategoryRequest request) {
        return categoryService.save(request);
    }
}
