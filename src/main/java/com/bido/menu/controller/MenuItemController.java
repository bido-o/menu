package com.bido.menu.controller;

import com.bido.menu.dto.menuitem.CreateMenuItemRequest;
import com.bido.menu.dto.menuitem.MenuItemResponseDto;
import com.bido.menu.dto.menuitem.MenuItemSummaryDto;
import com.bido.menu.dto.menuitem.UpdateMenuItemRequest;
import com.bido.menu.dto.variant.CreateMenuItemVariantRequest;
import com.bido.menu.dto.variant.MenuItemVariantResponseDto;
import com.bido.menu.dto.variant.UpdateMenuItemVariantRequest;
import com.bido.menu.security.AuthContext;
import com.bido.menu.service.MenuItemService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/menu/items")
public class MenuItemController {

    private static final Logger log = LoggerFactory.getLogger(MenuItemController.class);

    private final MenuItemService service;

    public MenuItemController(MenuItemService service) {
        this.service = service;
    }

    @GetMapping
    public List<MenuItemSummaryDto> list(@RequestParam(required = false) Long supplierId,
                                         @RequestParam(required = false) Long foodTypeId,
                                         @RequestParam(required = false) Long categoryId) {
        return service.findPublic(supplierId, foodTypeId, categoryId);
    }

    @GetMapping("/my")
    public List<MenuItemSummaryDto> listOwn(AuthContext auth) {
        if (!auth.isSupplier() && !auth.isAdmin()) {
            log.warn("Acces refuzat la listare items proprii: userId={}, role={}", auth.userId(), auth.role());
            throw AuthContext.forbidden();
        }
        return service.findByOwner(auth.userId());
    }

    @GetMapping("/{id}")
    public MenuItemResponseDto getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<MenuItemResponseDto> create(@Valid @RequestBody CreateMenuItemRequest dto,
                                                       @RequestParam(required = false) Long supplierId,
                                                       AuthContext auth,
                                                       UriComponentsBuilder uriBuilder) {
        Long targetSupplierId;
        if (auth.isAdmin()) {
            if (supplierId == null) throw new IllegalArgumentException("supplierId is required for admin");
            targetSupplierId = supplierId;
        } else if (auth.isSupplier()) {
            targetSupplierId = auth.userId();
        } else {
            log.warn("Acces refuzat la creare item: userId={}, role={}", auth.userId(), auth.role());
            throw AuthContext.forbidden();
        }

        MenuItemResponseDto created = service.create(dto, targetSupplierId);
        return ResponseEntity
                .created(uriBuilder.path("/api/menu/items/{id}").buildAndExpand(created.id()).toUri())
                .body(created);
    }

    @PostMapping("/{itemId}/variants")
    public ResponseEntity<MenuItemVariantResponseDto> addVariant(@PathVariable Long itemId,
                                                                 @Valid @RequestBody CreateMenuItemVariantRequest dto,
                                                                 AuthContext auth) {
        if (!auth.isSupplier() && !auth.isAdmin()) {
            log.warn("Acces refuzat la adaugare varianta pt item id={}: userId={}, role={}", itemId, auth.userId(), auth.role());
            throw AuthContext.forbidden();
        }

        MenuItemVariantResponseDto variant = service.addVariant(itemId, dto, auth);
        return ResponseEntity.status(HttpStatus.CREATED).body(variant);
    }

    @PatchMapping("/{id}")
    public MenuItemResponseDto update(@PathVariable Long id,
                                       @Valid @RequestBody UpdateMenuItemRequest dto,
                                       AuthContext auth) {
        if (!auth.isSupplier() && !auth.isAdmin()) {
            log.warn("Acces refuzat la actualizare item id={}: userId={}, role={}", id, auth.userId(), auth.role());
            throw AuthContext.forbidden();
        }
        return service.update(id, dto, auth);
    }

    @PatchMapping("/{itemId}/variants/{variantId}")
    public MenuItemVariantResponseDto updateVariant(@PathVariable Long itemId,
                                                    @PathVariable Long variantId,
                                                    @Valid @RequestBody UpdateMenuItemVariantRequest dto,
                                                    AuthContext auth) {
        if (!auth.isSupplier() && !auth.isAdmin()) {
            log.warn("Acces refuzat la actualizare varianta id={} pt item id={}: userId={}, role={}", variantId, itemId, auth.userId(), auth.role());
            throw AuthContext.forbidden();
        }
        return service.updateVariant(itemId, variantId, dto, auth);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, AuthContext auth) {
        if (!auth.isSupplier() && !auth.isAdmin()) {
            log.warn("Acces refuzat la stergere item id={}: userId={}, role={}", id, auth.userId(), auth.role());
            throw AuthContext.forbidden();
        }
        service.softDelete(id, auth);
    }

    @DeleteMapping("/{itemId}/variants/{variantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVariant(@PathVariable Long itemId, @PathVariable Long variantId, AuthContext auth) {
        if (!auth.isSupplier() && !auth.isAdmin()) {
            log.warn("Acces refuzat la stergere varianta id={} din item id={}: userId={}, role={}", variantId, itemId, auth.userId(), auth.role());
            throw AuthContext.forbidden();
        }
        service.deleteVariant(itemId, variantId, auth);
    }
}
