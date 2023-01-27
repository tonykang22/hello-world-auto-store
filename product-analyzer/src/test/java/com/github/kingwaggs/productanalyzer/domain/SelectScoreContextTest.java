package com.github.kingwaggs.productanalyzer.domain;

import com.github.kingwaggs.productanalyzer.util.PathFinder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SelectScoreContextTest {

    private final SelectScoreContext selectScoreContext = SelectScoreContext.createInitialContext(SelectScoreType.NAVER_CATEGORY);

    @BeforeAll
    static void init() {
        PathFinder pathFinder = new PathFinder();
        pathFinder.initRootPath("..");
    }

    @Test
    void nextSelectScore() {
        // Arrnage
        SelectScore selectScore1 = new SelectScore(1, "test1", 0.9, new ArrayList<>());
        SelectScore selectScore2 = new SelectScore(2, "test2", 0.8, new ArrayList<>());
        SelectScore selectScore3 = new SelectScore(3, "test3", 0.7, new ArrayList<>());
        SelectScore selectScore4 = new SelectScore(4, "test4", 0.6, new ArrayList<>());
        List<SelectScore> selectScoreList = Arrays.asList(selectScore1, selectScore2, selectScore3, selectScore4);
        LocalDate createdAt = LocalDate.now();

        selectScoreContext.loadContext(selectScoreList, createdAt);

        // Act
        // Assert
        assertEquals(selectScore1, selectScoreContext.nextSelectScore());
        assertEquals(selectScore2, selectScoreContext.nextSelectScore());
        assertEquals(selectScore3, selectScoreContext.nextSelectScore());
        assertEquals(selectScore4, selectScoreContext.nextSelectScore());
        assertEquals(selectScore1, selectScoreContext.nextSelectScore());
        assertEquals(createdAt, selectScoreContext.getDate());
        assertEquals(1, selectScoreContext.getCycle());
        assertEquals(1, selectScoreContext.getPointer());
    }

}