package com.bido.menu.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table( name = "ingredients",
        uniqueConstraints = { @UniqueConstraint( name = "uq_ingredient_name_per_supplier",
                                                 columnNames = {"name", "created_by"}) }
)
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

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
     * NULL means the ingredient was created by the platform.
     */
    @Column
    private Long createdBy;

    /*
     * Self-reference: if this ingredient was merged into another one, this points to the target.
     * NULL means it's a standalone / canonical ingredient.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merged_into_id")
    private Ingredient mergedIntoId;


    //getters & setters
    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Boolean getIsApproved() { return isApproved; }
    public void setIsApproved(Boolean isApproved) { this.isApproved = isApproved; }

    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

    public Ingredient getMergedIntoId() { return mergedIntoId; }
    public void setMergedIntoId(Ingredient mergedIntoId) { this.mergedIntoId = mergedIntoId; }

    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt;}

}
