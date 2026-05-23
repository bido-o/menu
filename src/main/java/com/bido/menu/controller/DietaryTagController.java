package com.bido.menu.controller;

import com.bido.menu.dto.DietaryTagDto;
import com.bido.menu.service.DietaryTagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menu/dietary-tags")
public class DietaryTagController {

    private final DietaryTagService service;

    public DietaryTagController(DietaryTagService service) {
        this.service = service;
    }

    @GetMapping
    public List<DietaryTagDto> getAll() {
        return service.findAll();
    }
}
