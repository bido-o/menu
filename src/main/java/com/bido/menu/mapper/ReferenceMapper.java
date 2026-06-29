package com.bido.menu.mapper;

import com.bido.menu.dto.DietaryTagDto;
import com.bido.menu.dto.FoodTypeDto;
import com.bido.menu.dto.UnitTypeDto;
import com.bido.menu.dto.category.ProductCategoryDto;
import com.bido.menu.entity.DietaryTag;
import com.bido.menu.entity.FoodType;
import com.bido.menu.entity.ProductCategory;
import com.bido.menu.entity.UnitType;
import org.springframework.stereotype.Component;

@Component
public class ReferenceMapper {

    public FoodTypeDto toFoodTypeDto(FoodType ft) {
        return new FoodTypeDto(ft.getId(), ft.getName(), ft.getImageUrl(), ft.getDisplayOrder());
    }

    public UnitTypeDto toUnitTypeDto(UnitType ut) {
        return new UnitTypeDto(ut.getId(), ut.getName());
    }

    public DietaryTagDto toDietaryTagDto(DietaryTag dt) {
        return new DietaryTagDto(dt.getId(), dt.getName(), dt.getIconUrl());
    }

    public ProductCategoryDto toCategoryDto(ProductCategory c) {
        return new ProductCategoryDto(c.getId(), c.getName(), c.getDisplayOrder());
    }
}
