package com.github.kingwaggs.productanalyzer.util;

import com.github.kingwaggs.productanalyzer.domain.FileType;
import com.github.kingwaggs.productanalyzer.domain.SelectScoreType;
import com.github.kingwaggs.productanalyzer.domain.product.AmazonSourcingProduct;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * For rootPath
 * - local env : ".."
 * - dev or prod : "/root/hello-world-auto-store"
 */
class FileUtilTest {

    private static final PathFinder pathFinder = new PathFinder();
    private static FileType fileType;
    private static SelectScoreType selectScoreType;

    @BeforeAll
    static void init() {
        pathFinder.initRootPath("..");
        fileType = FileType.SOURCING_RESULTS;
        selectScoreType = SelectScoreType.NAVER_CATEGORY;
    }

    @Test
    @DisplayName("복호화에 성공 시")
    void readFileSuccessfully() throws IOException {
        // given
        LocalDate latestDate = FileUtil.findLatestDate(fileType, selectScoreType);

        // when
        String decodedJson = FileUtil.readFile(fileType, selectScoreType, latestDate);

        // then
        assertNotNull(decodedJson);
    }

    @Test
    @DisplayName("복호화된 Json 파일 매핑 성공 시")
    void readValueSuccessfully() throws IOException {
        // given
        LocalDate latestDate = FileUtil.findLatestDate(fileType, selectScoreType);
        String decodedJson = FileUtil.readFile(fileType, selectScoreType, latestDate);

        // when
        List<AmazonSourcingProduct> result = (List<AmazonSourcingProduct>) FileUtil.readValue(fileType, decodedJson);

        // then
        assertTrue(!result.isEmpty());
    }

}