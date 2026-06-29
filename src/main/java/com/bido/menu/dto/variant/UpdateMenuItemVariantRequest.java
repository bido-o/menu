package com.bido.menu.dto.variant;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateMenuItemVariantRequest(
        @Size(max = 50) String label,
        BigDecimal quantity,
        @DecimalMin("0.01") BigDecimal price,
        Long unitTypeId,
        Integer sortOrder
) {}
