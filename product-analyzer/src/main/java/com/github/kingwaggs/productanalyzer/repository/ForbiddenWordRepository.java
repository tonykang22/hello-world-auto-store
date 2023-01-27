package com.github.kingwaggs.productanalyzer.repository;

import com.github.kingwaggs.productanalyzer.domain.ForbiddenWord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForbiddenWordRepository extends JpaRepository<ForbiddenWord, Long> {

    ForbiddenWord findByWord(String word);

    boolean existsByWord(String word);

}
