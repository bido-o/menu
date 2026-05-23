package com.bido.menu.repository;

import com.bido.menu.entity.MenuItemIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemIngredientRepository extends JpaRepository<MenuItemIngredient, Long> {
}
