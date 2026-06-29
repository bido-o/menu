package com.bido.menu.controller;

import com.bido.menu.dto.category.ProductCategoryDto;
import com.bido.menu.service.ProductCategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menu/categories")
public class ProductCategoryController {

    private final ProductCategoryService service;

    public ProductCategoryController(ProductCategoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductCategoryDto> getAll() {
        return service.findAllApproved();
    }
}
