package com.github.kingwaggs.productanalyzer.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.kingwaggs.productanalyzer.util.FileUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SelectScoreContext {

    private static final Integer INITIAL_SELECT_SCORE_LIST_SIZE = 300;
    private static final double INITIAL_RATIO = 0.5;
    private static final Integer INITIAL_INDEX = 0;
    private final SelectScoreType selectScoreType;
    private double sourcingRatio;
    private List<SelectScore> selectScoreList;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private int pointer;
    private int cycle;

    public static SelectScoreContext createInitialContext(SelectScoreType source) {
        List<SelectScore> initialSelectScoreList = createInitialList();
        LocalDate initialDate = LocalDate.MIN;
        try {
            LocalDate latestDate = FileUtil.findLatestDate(FileType.SELECT_SCORE_CONTEXT, source);
            String decodedJson = FileUtil.readFile(FileType.SELECT_SCORE_CONTEXT, source, latestDate);
            SelectScoreContextDto selectScoreContextDto = (SelectScoreContextDto) FileUtil.readValue(FileType.SELECT_SCORE_CONTEXT, decodedJson);
            int initialPointer = selectScoreContextDto.getPointer();
            int initialCycle = selectScoreContextDto.getCycle();
            double initialRatio = selectScoreContextDto.getRatio();
            return new SelectScoreContext(source, initialRatio, initialSelectScoreList,
                    initialDate, initialPointer, initialCycle);
        } catch (IOException e) {
            log.error("Exception occurred during creating initial select-score context.\n" +
                    "Returning new SelectScoreContext with pointer = 0, cycle = 0");
            return new SelectScoreContext(source, INITIAL_RATIO, initialSelectScoreList,
                    initialDate, INITIAL_INDEX, INITIAL_INDEX);
        }
    }

    public boolean isNewVersion(LocalDate targetDate) {
        return !date.isEqual(targetDate);
    }

    public void changeSourcingRatio(double ratio) {
        try {
            this.sourcingRatio = ratio;
            FileUtil.writeFile(FileType.SELECT_SCORE_CONTEXT, this.selectScoreType, LocalDate.now(),
                    new SelectScoreContextDto(this.pointer, this.cycle, this.sourcingRatio));
        } catch (IOException e) {
            log.error("Exception occurred during updating sourcing ration of SelectScoreType: {}", selectScoreType);
        }
    }

    public void initContext(List<SelectScore> selectScoreList, LocalDate createdAt) {
        this.selectScoreList = selectScoreList;
        this.date = createdAt;
        this.pointer = INITIAL_INDEX;
        this.cycle = INITIAL_INDEX;
    }

    public void loadContext(List<SelectScore> selectScoreList, LocalDate createdAt) {
        try {
            this.selectScoreList = selectScoreList;
            this.date = createdAt;

            SelectScoreContextDto selectScoreContextDto = readLatestPointer(selectScoreType);
            this.pointer = selectScoreContextDto.getPointer();
            this.cycle = selectScoreContextDto.getCycle();
            resetPointer();
        } catch (IOException exception) {
            log.error("Exception occurred during read pointerContext file. Use pointerContext's initial status. [pointer = 0, cycle = 0]");
        }
    }

    public SelectScore nextSelectScore() {
        try {
            int targetIdx = this.pointer;
            this.pointer++;
            resetPointer();
            FileUtil.writeFile(FileType.SELECT_SCORE_CONTEXT, this.selectScoreType, LocalDate.now(),
                    new SelectScoreContextDto(this.pointer, this.cycle, this.sourcingRatio));
            return selectScoreList.get(targetIdx);
        } catch (IOException exception) {
            log.error("Exception occurred during write pointerContext info to a file.");
            return selectScoreList.get(this.pointer);
        }
    }

    public void updatePointer(int pointer) {
        try {
            this.pointer = pointer;
            resetPointer();
            FileUtil.writeFile(FileType.SELECT_SCORE_CONTEXT, this.selectScoreType, LocalDate.now(),
                    new SelectScoreContextDto(this.pointer, this.cycle, this.sourcingRatio));
        } catch (IOException exception) {
            log.error("Exception occurred during write pointerContext info to a file.");
        }
    }

    private static List<SelectScore> createInitialList() {
        List<SelectScore> initialSelectScoreList = new ArrayList<>();
        for (int i = INITIAL_INDEX; i < INITIAL_SELECT_SCORE_LIST_SIZE; ++i) {
            initialSelectScoreList.add(new SelectScore());
        }
        return initialSelectScoreList;
    }

    private void resetPointer() {
        if (this.pointer >= selectScoreList.size()) {
            this.pointer = 0;
            this.cycle++;
        }
    }

    private SelectScoreContextDto readLatestPointer(SelectScoreType selectScoreType) throws IOException {
        LocalDate latestDate = FileUtil.findLatestDate(FileType.SELECT_SCORE_CONTEXT, selectScoreType);
        String decodedJson = FileUtil.readFile(FileType.SELECT_SCORE_CONTEXT, selectScoreType, latestDate);
        return (SelectScoreContextDto) FileUtil.readValue(FileType.SELECT_SCORE_CONTEXT, decodedJson);
    }

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SelectScoreContextDto {

        private int pointer;
        private int cycle;
        private double ratio;

    }
}
