package com.github.kingwaggs.productanalyzerv2.domain.entity;

import com.github.kingwaggs.productanalyzerv2.domain.SelectScoreType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "category")
@Getter
@NoArgsConstructor
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer categoryId;

    private String categoryName;

    @Enumerated(EnumType.STRING)
    private SelectScoreType type;

}
