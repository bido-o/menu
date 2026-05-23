package com.bido.menu.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
        name = "menu_item_ingredients",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_menu_item_ingredient",
                        columnNames = {"menu_item_id", "ingredient_id"}
                )
        }
)
public class MenuItemIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItemId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredientId;

    @Column(precision = 10, scale = 2)
    private BigDecimal quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_type_id")
    private UnitType unitTypeId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    //getters & setters
    public Long getId() { return id; }

    public MenuItem getMenuItem() { return menuItemId; }
    public void setMenuItem(MenuItem menuItemId) { this.menuItemId = menuItemId; }

    public Ingredient getIngredientId() { return ingredientId; }
    public void setIngredientId(Ingredient ingredientId) { this.ingredientId = ingredientId; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public UnitType getUnitTypeId() { return unitTypeId; }
    public void setUnitTypeId(UnitType unitTypeId) { this.unitTypeId = unitTypeId; }

    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAtAt() { return updatedAt; }
}
