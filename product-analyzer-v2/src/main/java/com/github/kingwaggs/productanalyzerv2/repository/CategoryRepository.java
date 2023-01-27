package com.github.kingwaggs.productanalyzerv2.repository;

import com.github.kingwaggs.productanalyzerv2.domain.SelectScoreType;
import com.github.kingwaggs.productanalyzerv2.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c.categoryId FROM category c WHERE c.type =?1")
    List<Integer> getCategoryIdList(SelectScoreType type);
}
