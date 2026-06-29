package com.bido.menu.dto.variant;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateMenuItemVariantRequest(
        @NotBlank @Size(max = 50) String label,
        BigDecimal quantity,
        @NotNull @DecimalMin("0.01") BigDecimal price,
        Long unitTypeId,
        @NotNull Integer sortOrder
) {}
