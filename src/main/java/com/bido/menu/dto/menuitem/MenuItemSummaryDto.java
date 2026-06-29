package com.bido.menu.dto.menuitem;

import java.math.BigDecimal;
import java.time.Instant;

public record MenuItemSummaryDto(
        Long id,
        String name,
        String description,
        String imageUrl,
        Boolean isAvailable,
        Long supplierProfileId,
        String foodTypeName,
        String productCategoryName,
        String dietaryTagName,
        BigDecimal startingPrice,
        Instant createdAt
) {}
