package com.bido.menu.repository;

import com.bido.menu.entity.DietaryTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DietaryTagRepository extends JpaRepository<DietaryTag, Long> {
}
