package com.bido.menu.controller;

import com.bido.menu.dto.FoodTypeDto;
import com.bido.menu.service.FoodTypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menu/food-types")
public class FoodTypeController {

    private final FoodTypeService service;

    public FoodTypeController(FoodTypeService service) {
        this.service = service;
    }

    @GetMapping
    public List<FoodTypeDto> getAll() {
        return service.findAll();
    }
}
