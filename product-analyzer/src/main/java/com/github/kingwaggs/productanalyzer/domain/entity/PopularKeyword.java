package com.github.kingwaggs.productanalyzer.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.kingwaggs.productanalyzer.domain.ProductType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "popular_keyword")
@ToString
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class PopularKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "keyword", nullable = false, unique = true)
    private String keyword;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "last_sourcing_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime lastSourcingAt;

    @Column
    @Enumerated(EnumType.STRING)
    private ProductType type;

    public void updateLastSourcingAt(LocalDateTime dateTime) {
        this.lastSourcingAt = dateTime;
    }

    public static PopularKeyword create(String keyword, ProductType type) {
        return PopularKeyword.builder()
                .keyword(keyword)
                .lastSourcingAt(LocalDateTime.of(LocalDate.EPOCH, LocalTime.MAX))
                .type(type)
                .build();
    }

}
