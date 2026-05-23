package com.bido.menu.repository;

import com.bido.menu.entity.MenuItemVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemVariantRepository extends JpaRepository<MenuItemVariant, Long> {
}
