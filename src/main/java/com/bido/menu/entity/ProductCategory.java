package com.bido.menu.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table( name = "product_categories",
        uniqueConstraints = { @UniqueConstraint( name = "uq_category_name_per_supplier",
                                                 columnNames = {"name", "created_by"}) }
)
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer displayOrder = 0;

    @Column(nullable = false)
    private Boolean isApproved = false;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    /*
     * Supplier profile id from the profile microservice.
     * NULL means the category was created by the platform.
     */
    @Column
    private Long createdBy;

    /*
     * Self-reference: if this category was merged into another one, this points to the target.
     * NULL means it's a standalone category.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merged_into_id")
    private ProductCategory mergedIntoId;


    //getters & setters
    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }

    public Boolean getIsApproved() { return isApproved; }
    public void setIsApproved(Boolean isApproved) { this.isApproved = isApproved;}

    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public ProductCategory getMergedInto() { return mergedIntoId; }
    public void setMergedInto(ProductCategory mergedInto) { this.mergedIntoId = mergedInto; }
}
