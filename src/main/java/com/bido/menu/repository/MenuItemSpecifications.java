package com.bido.menu.repository;

import com.bido.menu.entity.MenuItem;
import org.springframework.data.jpa.domain.Specification;

public final class MenuItemSpecifications {

    private MenuItemSpecifications() {}

    public static Specification<MenuItem> notDeleted() {
        return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
    }

    public static Specification<MenuItem> isAvailable() {
        return (root, query, cb) -> cb.isTrue(root.get("isAvailable"));
    }

    public static Specification<MenuItem> hasSupplier(Long supplierId) {
        return (root, query, cb) ->
                supplierId == null ? cb.conjunction() : cb.equal(root.get("supplierProfileId"), supplierId);
    }

    public static Specification<MenuItem> hasFoodType(Long foodTypeId) {
        return (root, query, cb) ->
                foodTypeId == null ? cb.conjunction() : cb.equal(root.get("foodType").get("id"), foodTypeId);
    }

    public static Specification<MenuItem> hasCategory(Long categoryId) {
        return (root, query, cb) ->
                categoryId == null ? cb.conjunction() : cb.equal(root.get("productCategory").get("id"), categoryId);
    }
}
