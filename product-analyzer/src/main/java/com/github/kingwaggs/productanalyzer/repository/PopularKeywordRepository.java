package com.github.kingwaggs.productanalyzer.repository;

import com.github.kingwaggs.productanalyzer.domain.entity.PopularKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PopularKeywordRepository extends JpaRepository<PopularKeyword, Long> {

    List<PopularKeyword> findByLastSourcingAtBefore(LocalDateTime localDateTime);

    PopularKeyword findByKeyword(String keyword);

    boolean existsByKeyword(String keyword);

}
