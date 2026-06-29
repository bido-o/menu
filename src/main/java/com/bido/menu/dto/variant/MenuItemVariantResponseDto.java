package com.bido.menu.dto.variant;

import com.bido.menu.dto.UnitTypeDto;

import java.math.BigDecimal;

public record MenuItemVariantResponseDto(
        Long id,
        String label,
        BigDecimal quantity,
        BigDecimal price,
        UnitTypeDto unitType,
        Integer sortOrder
) {}
