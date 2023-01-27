package com.github.kingwaggs.productanalyzer.repository;

import com.github.kingwaggs.productanalyzer.domain.entity.ForbiddenIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForbiddenIngredientRepository extends JpaRepository<ForbiddenIngredient, Long> {
}
