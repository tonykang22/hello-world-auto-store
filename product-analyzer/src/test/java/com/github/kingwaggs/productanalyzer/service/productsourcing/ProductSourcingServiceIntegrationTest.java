package com.github.kingwaggs.productanalyzer.service.productsourcing;

import com.github.kingwaggs.productanalyzer.config.TestConfig;
import com.github.kingwaggs.productanalyzer.domain.ProductType;
import com.github.kingwaggs.productanalyzer.domain.dto.request.PopularKeywordRequestDto;
import com.github.kingwaggs.productanalyzer.domain.dto.result.AnalyzedResultDto;
import com.github.kingwaggs.productanalyzer.domain.product.AmazonSourcingProduct;
import com.github.kingwaggs.productanalyzer.service.PopularKeywordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest(properties = "spring.profiles.active=local")
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductSourcingServiceIntegrationTest {

    @Autowired
    private ProductSourcingService productSourcingService;

    @Autowired
    private PopularKeywordService popularKeywordService;

    @Test
    void createPopularKeywordSourcingResultsSuccessfully() {
        // given
        PopularKeywordRequestDto.PopularKeywordDto popularKeywordDto
                = new PopularKeywordRequestDto.PopularKeywordDto("hair loss", ProductType.ETC);
        popularKeywordService.savePopularKeywords(List.of(popularKeywordDto));

        // when
        AnalyzedResultDto resultDto = productSourcingService.createPopularKeywordSourcingResults();

        @SuppressWarnings("unchecked")
        List<AmazonSourcingProduct> result = (List<AmazonSourcingProduct>) resultDto.getResult();

        // then
        assertNotNull(result);
    }

}