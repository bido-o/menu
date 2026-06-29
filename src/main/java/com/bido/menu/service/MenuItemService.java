package com.bido.menu.service;

import com.bido.menu.dto.menuitem.CreateMenuItemRequest;
import com.bido.menu.dto.menuitem.MenuItemResponseDto;
import com.bido.menu.dto.menuitem.MenuItemSummaryDto;
import com.bido.menu.dto.menuitem.UpdateMenuItemRequest;
import com.bido.menu.dto.variant.CreateMenuItemVariantRequest;
import com.bido.menu.dto.variant.MenuItemVariantResponseDto;
import com.bido.menu.dto.variant.UpdateMenuItemVariantRequest;
import com.bido.menu.entity.*;
import com.bido.menu.exception.ResourceNotFoundException;
import com.bido.menu.mapper.MenuItemMapper;
import com.bido.menu.repository.*;
import com.bido.menu.security.AuthContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class MenuItemService {

    private static final Logger log = LoggerFactory.getLogger(MenuItemService.class);

    private final MenuItemRepository menuItemRepository;
    private final MenuItemVariantRepository variantRepository;
    private final FoodTypeRepository foodTypeRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final DietaryTagRepository dietaryTagRepository;
    private final UnitTypeRepository unitTypeRepository;
    private final MenuItemMapper mapper;

    public MenuItemService(MenuItemRepository menuItemRepository,
                           MenuItemVariantRepository variantRepository,
                           FoodTypeRepository foodTypeRepository,
                           ProductCategoryRepository productCategoryRepository,
                           DietaryTagRepository dietaryTagRepository,
                           UnitTypeRepository unitTypeRepository,
                           MenuItemMapper mapper) {
        this.menuItemRepository = menuItemRepository;
        this.variantRepository = variantRepository;
        this.foodTypeRepository = foodTypeRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.dietaryTagRepository = dietaryTagRepository;
        this.unitTypeRepository = unitTypeRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<MenuItemSummaryDto> findPublic(Long supplierId, Long foodTypeId, Long categoryId) {
        log.debug("Cautare publica items: supplierId={}, foodTypeId={}, categoryId={}", supplierId, foodTypeId, categoryId);
        Specification<MenuItem> spec = Specification.allOf(
                MenuItemSpecifications.notDeleted(),
                MenuItemSpecifications.isAvailable(),
                MenuItemSpecifications.hasSupplier(supplierId),
                MenuItemSpecifications.hasFoodType(foodTypeId),
                MenuItemSpecifications.hasCategory(categoryId));

        List<MenuItemSummaryDto> results = menuItemRepository.findAll(spec).stream()
                .map(this::toSummaryWithPrice)
                .toList();

        log.debug("Gasite {} items", results.size());
        return results;
    }

    @Transactional(readOnly = true)
    public List<MenuItemSummaryDto> findByOwner(Long supplierProfileId) {
        log.debug("Listare items proprii pt supplierProfileId={}", supplierProfileId);

        return menuItemRepository.findAllBySupplierProfileIdAndDeletedAtIsNull(supplierProfileId)
                .stream()
                .map(this::toSummaryWithPrice)
                .toList();
    }

    @Transactional(readOnly = true)
    public MenuItemResponseDto findById(Long id) {
        MenuItem item = menuItemRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", id));

        List<MenuItemVariant> variants = variantRepository.findAllByMenuItemIdOrderBySortOrderAsc(id);
        return mapper.toResponseDto(item, variants);
    }

    @Transactional
    public MenuItemResponseDto create(CreateMenuItemRequest dto, Long supplierProfileId) {
        log.info("Creare menu item '{}' de catre supplierProfileId={}", dto.name(), supplierProfileId);

        FoodType foodType = foodTypeRepository.findById(dto.foodTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("FoodType", dto.foodTypeId()));
        ProductCategory category = productCategoryRepository.findById(dto.productCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("ProductCategory", dto.productCategoryId()));
        DietaryTag dietaryTag = resolveDietaryTag(dto.dietaryTagId());

        MenuItem item = mapper.toEntity(dto, foodType, category, dietaryTag, supplierProfileId);
        item = menuItemRepository.save(item);

        List<MenuItemVariant> variants = createVariants(dto.variants(), item);
        log.info("Menu item creat cu id={} si {} variante", item.getId(), variants.size());

        return mapper.toResponseDto(item, variants);
    }

    @Transactional
    public MenuItemResponseDto update(Long id, UpdateMenuItemRequest dto, AuthContext auth) {
        log.info("Actualizare menu item id={} de catre userId={}", id, auth.userId());
        MenuItem item = menuItemRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", id));
        auth.requireSupplierOwnerOrAdmin(item.getSupplierProfileId());

        if (dto.name() != null) item.setName(dto.name());
        if (dto.description() != null) item.setDescription(dto.description());
        if (dto.imageUrl() != null) item.setImageUrl(dto.imageUrl());
        if (dto.isAvailable() != null) item.setIsAvailable(dto.isAvailable());

        if (dto.foodTypeId() != null) {
            item.setFoodType(foodTypeRepository.findById(dto.foodTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("FoodType", dto.foodTypeId())));
        }
        if (dto.productCategoryId() != null) {
            item.setProductCategory(productCategoryRepository.findById(dto.productCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("ProductCategory", dto.productCategoryId())));
        }
        if (dto.dietaryTagId() != null) {
            item.setDietaryTag(dietaryTagRepository.findById(dto.dietaryTagId())
                    .orElseThrow(() -> new ResourceNotFoundException("DietaryTag", dto.dietaryTagId())));
        }

        item = menuItemRepository.save(item);
        List<MenuItemVariant> variants = variantRepository.findAllByMenuItemIdOrderBySortOrderAsc(id);
        return mapper.toResponseDto(item, variants);
    }

    @Transactional
    public void softDelete(Long id, AuthContext auth) {
        log.info("Soft-delete menu item id={} de catre userId={}", id, auth.userId());
        MenuItem item = menuItemRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", id));
        auth.requireSupplierOwnerOrAdmin(item.getSupplierProfileId());

        item.setDeletedAt(Instant.now());
        menuItemRepository.save(item);
        log.info("Menu item id={} sters (soft-delete)", id);
    }

    @Transactional
    public MenuItemVariantResponseDto addVariant(Long itemId, CreateMenuItemVariantRequest dto, AuthContext auth) {
        log.info("Adaugare varianta '{}' la item id={} de catre userId={}", dto.label(), itemId, auth.userId());
        MenuItem item = menuItemRepository.findByIdAndDeletedAtIsNull(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", itemId));

        auth.requireSupplierOwnerOrAdmin(item.getSupplierProfileId());

        MenuItemVariant variant = mapper.toVariantEntity(dto, item, resolveUnitType(dto.unitTypeId()));
        variant = variantRepository.save(variant);
        log.info("Varianta creata cu id={} pt item id={}", variant.getId(), itemId);
        return mapper.toVariantDto(variant);
    }

    @Transactional
    public MenuItemVariantResponseDto updateVariant(Long itemId, Long variantId, UpdateMenuItemVariantRequest dto, AuthContext auth) {
        MenuItem item = menuItemRepository.findByIdAndDeletedAtIsNull(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", itemId));
        auth.requireSupplierOwnerOrAdmin(item.getSupplierProfileId());

        MenuItemVariant variant = findVariantOfItem(variantId, itemId);

        if (dto.label() != null) variant.setLabel(dto.label());
        if (dto.quantity() != null) variant.setQuantity(dto.quantity());
        if (dto.price() != null) variant.setPrice(dto.price());
        if (dto.sortOrder() != null) variant.setSortOrder(dto.sortOrder());
        if (dto.unitTypeId() != null) variant.setUnitType(resolveUnitType(dto.unitTypeId()));

        variant = variantRepository.save(variant);
        return mapper.toVariantDto(variant);
    }

    @Transactional
    public void deleteVariant(Long itemId, Long variantId, AuthContext auth) {
        log.info("Stergere varianta id={} din item id={} de catre userId={}", variantId, itemId, auth.userId());
        MenuItem item = menuItemRepository.findByIdAndDeletedAtIsNull(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", itemId));
        auth.requireSupplierOwnerOrAdmin(item.getSupplierProfileId());

        MenuItemVariant variant = findVariantOfItem(variantId, itemId);
        variantRepository.delete(variant);
    }

    private MenuItemSummaryDto toSummaryWithPrice(MenuItem item) {
        BigDecimal startingPrice = variantRepository.findMinPriceByMenuItemId(item.getId());
        return mapper.toSummaryDto(item, startingPrice);
    }

    private List<MenuItemVariant> createVariants(List<CreateMenuItemVariantRequest> variants, MenuItem item) {
        return variants.stream()
                .map(dtoVariant -> variantRepository.save(mapper.toVariantEntity(dtoVariant, item, resolveUnitType(dtoVariant.unitTypeId()))))
                .toList();
    }

    private MenuItemVariant findVariantOfItem(Long variantId, Long itemId) {
        MenuItemVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItemVariant", variantId));

        if (!variant.getMenuItem().getId().equals(itemId)) {
            throw new ResourceNotFoundException("MenuItemVariant", variantId);
        }

        return variant;
    }

    private DietaryTag resolveDietaryTag(Long dietaryTagId) {
        if (dietaryTagId == null) return null;
        return dietaryTagRepository.findById(dietaryTagId)
                .orElseThrow(() -> new ResourceNotFoundException("DietaryTag", dietaryTagId));
    }

    private UnitType resolveUnitType(Long unitTypeId) {
        if (unitTypeId == null) return null;
        return unitTypeRepository.findById(unitTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("UnitType", unitTypeId));
    }
}
