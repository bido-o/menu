package com.bido.menu.repository;

import com.bido.menu.entity.MenuItemVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MenuItemVariantRepository extends JpaRepository<MenuItemVariant, Long> {

    List<MenuItemVariant> findAllByMenuItemIdOrderBySortOrderAsc(Long menuItemId);

    @Query("SELECT MIN(v.price) FROM MenuItemVariant v WHERE v.menuItem.id = :menuItemId")
    BigDecimal findMinPriceByMenuItemId(Long menuItemId);
}
