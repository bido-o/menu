package com.bido.menu.dto.menuitem;

import com.bido.menu.dto.DietaryTagDto;
import com.bido.menu.dto.FoodTypeDto;
import com.bido.menu.dto.category.ProductCategoryDto;
import com.bido.menu.dto.variant.MenuItemVariantResponseDto;

import java.time.Instant;
import java.util.List;

public record MenuItemResponseDto(
        Long id,
        String name,
        String description,
        String imageUrl,
        Boolean isAvailable,
        Long supplierProfileId,
        FoodTypeDto foodType,
        ProductCategoryDto productCategory,
        DietaryTagDto dietaryTag,
        List<MenuItemVariantResponseDto> variants,
        Instant createdAt
) {}
