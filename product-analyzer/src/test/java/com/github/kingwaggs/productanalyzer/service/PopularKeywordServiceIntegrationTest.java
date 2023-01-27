package com.github.kingwaggs.productanalyzer.service;

import com.github.kingwaggs.productanalyzer.config.TestConfig;
import com.github.kingwaggs.productanalyzer.domain.ProductType;
import com.github.kingwaggs.productanalyzer.domain.dto.request.PopularKeywordRequestDto;
import com.github.kingwaggs.productanalyzer.domain.entity.PopularKeyword;
import com.github.kingwaggs.productanalyzer.repository.PopularKeywordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DataJpaTest(properties = "spring.profiles.active=local")
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PopularKeywordServiceIntegrationTest {

    @Autowired
    private PopularKeywordService popularKeywordService;

    @Autowired
    private PopularKeywordRepository popularKeywordRepository;

    @Test @DisplayName("초기 날짜는 1970.01.01. 23:59:59")
    void notUpdateLastSourcingAt() {
        // given
        PopularKeywordRequestDto.PopularKeywordDto popularKeywordDto
                = new PopularKeywordRequestDto.PopularKeywordDto("Test", ProductType.ETC);

        popularKeywordService.savePopularKeywords(List.of(popularKeywordDto));

        // when
        PopularKeyword foundKeyword = popularKeywordRepository.findByKeyword(popularKeywordDto.getKeyword());

        // then
        assertEquals(LocalDateTime.of(LocalDate.EPOCH, LocalTime.MAX), foundKeyword.getLastSourcingAt());
    }

    @Test @DisplayName("조회 시 마지막 사용 날짜 업데이트")
    void updateLastSourcingAt() {
        // given
        PopularKeywordRequestDto.PopularKeywordDto popularKeywordDto
                = new PopularKeywordRequestDto.PopularKeywordDto("Test", ProductType.ETC);

        popularKeywordService.savePopularKeywords(List.of(popularKeywordDto));

        // when
        popularKeywordService.getTargetPopularKeywords();

        PopularKeyword foundKeyword = popularKeywordRepository.findByKeyword(popularKeywordDto.getKeyword());

        // then
        assertNotEquals(LocalDateTime.of(LocalDate.EPOCH, LocalTime.MAX), foundKeyword.getLastSourcingAt());
    }

}