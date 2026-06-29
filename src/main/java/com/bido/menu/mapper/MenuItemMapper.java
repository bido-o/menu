package com.bido.menu.mapper;

import com.bido.menu.dto.menuitem.CreateMenuItemRequest;
import com.bido.menu.dto.menuitem.MenuItemResponseDto;
import com.bido.menu.dto.menuitem.MenuItemSummaryDto;
import com.bido.menu.dto.variant.CreateMenuItemVariantRequest;
import com.bido.menu.dto.variant.MenuItemVariantResponseDto;
import com.bido.menu.entity.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class MenuItemMapper {

    private final ReferenceMapper referenceMapper;

    public MenuItemMapper(ReferenceMapper referenceMapper) {
        this.referenceMapper = referenceMapper;
    }

    public MenuItem toEntity(CreateMenuItemRequest dto, FoodType foodType,
                             ProductCategory category, DietaryTag dietaryTag, Long supplierProfileId) {
        MenuItem item = new MenuItem();
        item.setName(dto.name());
        item.setDescription(dto.description());
        item.setImageUrl(dto.imageUrl());
        item.setSupplierProfileId(supplierProfileId);
        item.setFoodType(foodType);
        item.setProductCategory(category);
        item.setDietaryTag(dietaryTag);
        return item;
    }

    public MenuItemVariant toVariantEntity(CreateMenuItemVariantRequest dto, MenuItem item, UnitType unitType) {
        MenuItemVariant v = new MenuItemVariant();
        v.setLabel(dto.label());
        v.setQuantity(dto.quantity());
        v.setPrice(dto.price());
        v.setSortOrder(dto.sortOrder() != null ? dto.sortOrder() : 0);
        v.setMenuItem(item);
        v.setUnitType(unitType);
        return v;
    }

    public MenuItemResponseDto toResponseDto(MenuItem item, List<MenuItemVariant> variants) {
        return new MenuItemResponseDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getImageUrl(),
                item.getIsAvailable(),
                item.getSupplierProfileId(),
                referenceMapper.toFoodTypeDto(item.getFoodType()),
                referenceMapper.toCategoryDto(item.getProductCategory()),
                item.getDietaryTag() != null ? referenceMapper.toDietaryTagDto(item.getDietaryTag()) : null,
                variants.stream().map(this::toVariantDto).toList(),
                item.getCreatedAt()
        );
    }

    public MenuItemSummaryDto toSummaryDto(MenuItem item, BigDecimal startingPrice) {
        return new MenuItemSummaryDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getImageUrl(),
                item.getIsAvailable(),
                item.getSupplierProfileId(),
                item.getFoodType().getName(),
                item.getProductCategory().getName(),
                item.getDietaryTag() != null ? item.getDietaryTag().getName() : null,
                startingPrice,
                item.getCreatedAt()
        );
    }

    public MenuItemVariantResponseDto toVariantDto(MenuItemVariant v) {
        UnitType unit = v.getUnitType();
        return new MenuItemVariantResponseDto(
                v.getId(),
                v.getLabel(),
                v.getQuantity(),
                v.getPrice(),
                unit != null ? referenceMapper.toUnitTypeDto(unit) : null,
                v.getSortOrder()
        );
    }
}
