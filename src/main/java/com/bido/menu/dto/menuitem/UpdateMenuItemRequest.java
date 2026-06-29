package com.bido.menu.dto.menuitem;

import jakarta.validation.constraints.Size;

public record UpdateMenuItemRequest(
        @Size(max = 200) String name,
        String description,
        String imageUrl,
        Long foodTypeId,
        Long productCategoryId,
        Long dietaryTagId,
        Boolean isAvailable
) {}
