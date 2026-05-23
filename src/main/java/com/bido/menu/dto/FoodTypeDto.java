package com.bido.menu.dto;

public record FoodTypeDto(
        Long id,
        String name,
        String imageUrl,
        Integer displayOrder
) {}
