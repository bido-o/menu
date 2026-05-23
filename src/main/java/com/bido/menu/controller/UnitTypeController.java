package com.bido.menu.controller;

import com.bido.menu.dto.UnitTypeDto;
import com.bido.menu.service.UnitTypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menu/unit-types")
public class UnitTypeController {

    private final UnitTypeService service;

    public UnitTypeController(UnitTypeService service) {
        this.service = service;
    }

    @GetMapping
    public List<UnitTypeDto> getAll() {
        return service.findAll();
    }
}
