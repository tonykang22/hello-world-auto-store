package com.github.kingwaggs.productanalyzer.service.selectscore;

import com.github.kingwaggs.productanalyzer.domain.FileType;
import com.github.kingwaggs.productanalyzer.domain.SelectScore;
import com.github.kingwaggs.productanalyzer.domain.SelectScoreFactory;
import com.github.kingwaggs.productanalyzer.domain.SelectScoreType;
import com.github.kingwaggs.productanalyzer.domain.dto.ItemScoutIndicatorDto;
import com.github.kingwaggs.productanalyzer.domain.dto.result.AnalyzedResultDto;
import com.github.kingwaggs.productanalyzer.domain.dto.result.ErrorResultDto;
import com.github.kingwaggs.productanalyzer.domain.dto.result.SelectScoreResultDto;
import com.github.kingwaggs.productanalyzer.exception.AlreadyWorkingException;
import com.github.kingwaggs.productanalyzer.exception.SelectScoreException;
import com.github.kingwaggs.productanalyzer.service.selectscore.job.ItemScoutWebScrapingWorker;
import com.github.kingwaggs.productanalyzer.service.selectscore.job.NaverCategoryReader;
import com.github.kingwaggs.productanalyzer.service.selectscore.job.PandaRankWebScraper;
import com.github.kingwaggs.productanalyzer.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SelectScoreService {

    private final NaverCategoryReader naverCategoryReader;
    private final PandaRankWebScraper pandaRankWebScraper;

    private final ItemScoutWebScrapingWorker itemScoutWebScrapingWorker;
    private final SelectScoreFactory selectScoreFactory;

    public AnalyzedResultDto loadSelectScore(SelectScoreType selectScoreType, LocalDate date, Integer size) {
        try {
            if (date == null) {
                date = FileUtil.findLatestDate(FileType.SELECT_SCORES, selectScoreType);
            }
            if (!FileUtil.isExist(FileType.SELECT_SCORES, selectScoreType, date)) {
                String statusMessage;
                Exception exception;
                if (naverCategoryReader.isRunning()) {
                    statusMessage = naverCategoryReader.getStatusMessage();
                    exception = new AlreadyWorkingException(statusMessage);
                } else if (pandaRankWebScraper.isRunning()) {
                    statusMessage = pandaRankWebScraper.getStatusMessage();
                    exception = new AlreadyWorkingException(statusMessage);
                } else if (itemScoutWebScrapingWorker.isRunning()) {
                    statusMessage = itemScoutWebScrapingWorker.getStatusMessage();
                    exception = new AlreadyWorkingException(statusMessage);
                } else {
                    statusMessage = String.format("The File not exists. (selectScoreType : %s, date : %s)", selectScoreType, date);
                    exception = new IllegalArgumentException(statusMessage);
                }
                log.info(statusMessage);
                return new ErrorResultDto(exception, date);
            }
            String decodedJson = FileUtil.readFile(FileType.SELECT_SCORES, selectScoreType, date);
            Object mappedJson = FileUtil.readValue(FileType.SELECT_SCORES, decodedJson);
            List<SelectScore> selectScoreList = (List<SelectScore>) mappedJson;
            selectScoreList = size == null ? selectScoreList : selectScoreList.subList(0, size);
            return new SelectScoreResultDto(selectScoreList, date);
        } catch (Exception exception) {
            log.error("Exception occurred while loadSelectScore.", exception);
            return new ErrorResultDto(exception, date);
        }
    }

    public AnalyzedResultDto createSelectScore(SelectScoreType source, Integer size) {
        try {
            List<String> searchWordList = createSearchWordList(source);
            if (size != null) {
                searchWordList = createRandomList(searchWordList, size);
            }
            List<ItemScoutIndicatorDto> itemScoutIndicatorDtoList = itemScoutWebScrapingWorker.submit(searchWordList, source.getType());
            List<SelectScore> selectScoreList = selectScoreFactory.create(itemScoutIndicatorDtoList);
            if (selectScoreList.isEmpty()) {
                String errorMessage = "Creating select score FAILED for empty scraping result. 'Satisfy Threshold' may have been applied.";
                log.error(errorMessage);
                return new ErrorResultDto(new SelectScoreException(errorMessage), LocalDate.now());
            }
            log.info("Creating select score successfully. (source : {}, size : {})", source.getType(), selectScoreList.size());
            return new SelectScoreResultDto(selectScoreList, LocalDate.now());
        } catch (Exception exception) {
            log.error("Exception occurred while createSelectScore.", exception);
            return new ErrorResultDto(exception, LocalDate.now());
        }
    }

    public LocalDate getLatestVersion(SelectScoreType selectScoreType) {
        try {
            return FileUtil.findLatestDate(FileType.SELECT_SCORES, selectScoreType);
        } catch (FileNotFoundException exception) {
            log.error("There's no select score file for {}. return null.", selectScoreType.getType());
            return null;
        }
    }

    private List<String> createSearchWordList(SelectScoreType source) throws AlreadyWorkingException {
        List<String> searchWordList;
        switch (source) {
            case NAVER_CATEGORY:
                searchWordList = naverCategoryReader.createSearchWordList();
                break;
            case PANDA_RANK:
                searchWordList = pandaRankWebScraper.createSearchWordList();
                break;
            default:
                throw new IllegalArgumentException();
        }
        return searchWordList;
    }

    private List<String> createRandomList(List<String> initialList, Integer size) {
        List<String> randomList = new ArrayList<>();
        Random random = new Random();
        int upperbound = initialList.size();
        Set<Integer> duplicateCheckSet = new HashSet<>();
        while (randomList.size() < size) {
            int candidateIndex = random.nextInt(upperbound);
            if (duplicateCheckSet.contains(candidateIndex)) {
                continue;
            }
            String searchWord = initialList.get(candidateIndex);
            randomList.add(searchWord);
            duplicateCheckSet.add(candidateIndex);
        }
        return randomList;
    }

}
