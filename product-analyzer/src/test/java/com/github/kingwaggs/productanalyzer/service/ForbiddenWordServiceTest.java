package com.github.kingwaggs.productanalyzer.service;

import com.github.kingwaggs.productanalyzer.config.TestConfig;
import com.github.kingwaggs.productanalyzer.domain.ForbiddenWord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DataJpaTest(properties = "spring.profiles.active:local")
@Import(value = TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ForbiddenWordServiceTest {

    @Autowired
    private ForbiddenWordService forbiddenWordService;

    @Test
    @DisplayName("중복된 단어는 저장하지 않는다.")
    void doesNotSaveDuplicatedWords() {
        // given
        List<String> forbiddenWordList = List.of("전자", "애플", "apple");

        // when
        List<ForbiddenWord> savedFirst = forbiddenWordService.saveForbiddenWords(forbiddenWordList);
        List<ForbiddenWord> savedSecond = forbiddenWordService.saveForbiddenWords(forbiddenWordList);

        // then
        assertThat(savedFirst).hasSize(3);
        assertThat(savedSecond).isEmpty();
    }

    @Test
    @DisplayName("DB에 없는 단어라면 무시한다.")
    void deleteWordsOnlyInDB() {
        // given
        List<String> forbiddenWordList = List.of("전자", "애플", "apple");

        // when

        // then
        assertThatNoException().isThrownBy(() -> forbiddenWordService.deleteForbiddenWords(forbiddenWordList));
    }

}