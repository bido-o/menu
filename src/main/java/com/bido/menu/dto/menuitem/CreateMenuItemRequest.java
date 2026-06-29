package com.bido.menu.dto.menuitem;

import com.bido.menu.dto.variant.CreateMenuItemVariantRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateMenuItemRequest(
        @NotBlank @Size(max = 200) String name,
        String description,
        String imageUrl,
        @NotNull Long foodTypeId,
        @NotNull Long productCategoryId,
        Long dietaryTagId,
        @NotEmpty @Valid List<CreateMenuItemVariantRequest> variants
) {}
