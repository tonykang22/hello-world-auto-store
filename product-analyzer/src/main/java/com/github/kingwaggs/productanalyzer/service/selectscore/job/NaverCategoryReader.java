package com.github.kingwaggs.productanalyzer.service.selectscore.job;

import com.github.kingwaggs.productanalyzer.domain.dto.NaverCategoryDto;
import com.github.kingwaggs.productanalyzer.exception.AlreadyWorkingException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Slf4j
@Getter(value = AccessLevel.PROTECTED)
@Service
public class NaverCategoryReader implements SelectScoreJob {
    private static final String FIXED_FILE_PATH = "static/naver_category_id_list_for_amazon_sourcing.xlsx";
    private static final int FIRST_ROW_INDEX = 0;
    private static final int FIXED_SHEET_INDEX = 0;
    private static final int FIXED_CELL_SIZE = 5;
    private static final String EMPTY_STRING = "";

    String LOGGING_MESSAGE_SUCCEED = "Validating search term \"{}\" category SUCCEED. Return true. (category : {})";
    String LOGGING_MESSAGE_FAILED = "Validating search term \"{}\" category FAILED. Matching category is not in the filtered naver category. Return false. (category : {})";

    private final List<NaverCategoryDto> naverCategoryList = readCategoryList();
    private final Map<String, Map<String, Map<String, Set<String>>>> naverCategoryMap = createCategoryMap(this.naverCategoryList);

    private volatile AtomicBoolean running = new AtomicBoolean(false);
    private volatile LocalDateTime startedAt = LocalDateTime.MIN;

    // TODO : 횡단관심사 AOP Refactoring
    public List<String> createSearchWordList() throws AlreadyWorkingException {
        if (isRunning()) {
            String message = getStatusMessage();
            throw new AlreadyWorkingException(message);
        }
        lockIndicators();
        List<String> searchWordList = collectSearchWords();
        unlockIndicators();
        log.info("createSearchWordList from Naver Category successfully.(keywords : {})", searchWordList.size());
        return searchWordList;
    }

    public boolean contains(List<String> categoryList, String searchTerm) {
        if(categoryList.size() > 0) {
            String depth1 = categoryList.get(0);
            Map<String, Map<String, Map<String, Set<String>>>> depth1Map = this.naverCategoryMap;
            if(!depth1Map.containsKey(depth1)) {
                log.info(LOGGING_MESSAGE_FAILED, depth1);
                return false;
            }
            if(categoryList.size() > 1) {
                String depth2 = categoryList.get(1);
                Map<String, Map<String, Set<String>>> depth2Map = depth1Map.get(depth1);
                if(!depth2Map.containsKey(depth2)) {
                    log.info(LOGGING_MESSAGE_FAILED, depth2);
                    return false;
                }
                if(categoryList.size() > 2) {
                    String depth3 = categoryList.get(2);
                    Map<String, Set<String>> depth3Map = depth2Map.get(depth2);
                    if(!depth3Map.containsKey(depth3)) {
                        log.info(LOGGING_MESSAGE_FAILED, depth3);
                        return false;
                    }
                    if(categoryList.size() > 3) {
                        String depth4 = categoryList.get(3);
                        Set<String> depth4Set = depth3Map.get(depth3);
                        if(!depth4Set.contains(depth4)) {
                            log.info(LOGGING_MESSAGE_FAILED, depth4);
                            return false;
                        }
                    }
                }
            }
        }
        log.info(LOGGING_MESSAGE_SUCCEED, searchTerm, String.join(">", categoryList));
        return true;
    }

    @Override
    public boolean isRunning() {
        return this.running.get();
    }

    @Override
    public String getStatusMessage() {
        String currentStatus = String.format("startedAt : %s", this.startedAt);
        return String.format("NaverCategoryFileReader is already working(%s)", currentStatus);
    }

    private Map<String, Map<String, Map<String, Set<String>>>> createCategoryMap(List<NaverCategoryDto> categoryList) {
        Map<String, Map<String, Map<String, Set<String>>>> depth1Map = new LinkedHashMap<>();
        for (NaverCategoryDto category : categoryList) {
            String depth1 = category.getDepth1();
            if (!depth1Map.containsKey(depth1)) {
                depth1Map.put(depth1, new LinkedHashMap<>());
            }
            Map<String, Map<String, Set<String>>> depth2Map = depth1Map.get(depth1);
            String depth2 = category.getDepth2();
            if (!depth2Map.containsKey(depth2)) {
                depth2Map.put(depth2, new LinkedHashMap<>());
            }
            Map<String, Set<String>> depth3Map = depth2Map.get(depth2);
            String depth3 = category.getDepth3();
            if (!depth3Map.containsKey(depth3)) {
                depth3Map.put(depth3, new LinkedHashSet<>());
            }
            Set<String> depth4Set = depth3Map.get(depth3);
            String depth4 = category.getDepth4();
            if (!depth4.equals(EMPTY_STRING)) {
                depth4Set.add(depth4);
            }
            if (!depth3.equals(EMPTY_STRING)) {
                depth3Map.put(depth3, depth4Set);
            }
            if (!depth2.equals(EMPTY_STRING)) {
                depth2Map.put(depth2, depth3Map);
            }
            if (!depth1.equals(EMPTY_STRING)) {
                depth1Map.put(depth1, depth2Map);
            }
        }
        return depth1Map;
    }

    private List<String> collectSearchWords() {
        Set<String> searchWordSet = makeSearchWords(this.naverCategoryList);
        return new ArrayList<>(searchWordSet);
    }

    private Set<String> makeSearchWords(List<NaverCategoryDto> naverCategoryDtoList) {
        Set<String> searchWordSet = new HashSet<>();
        for (NaverCategoryDto naverCategoryDto : naverCategoryDtoList) {
            String searchWord;
            if (naverCategoryDto.getDepth3() == null || naverCategoryDto.getDepth3().isBlank()) {
                searchWord = String.join("/", naverCategoryDto.getDepth1(), naverCategoryDto.getDepth2()).trim();
            } else if (naverCategoryDto.getDepth4() == null || naverCategoryDto.getDepth4().isBlank()) {
                searchWord = String.join("/", naverCategoryDto.getDepth2(), naverCategoryDto.getDepth3()).trim();
            } else {
                searchWord = String.join("/", naverCategoryDto.getDepth3(), naverCategoryDto.getDepth4()).trim();
            }
            String replacedSearchTerm = searchWord.replaceAll(" ", "");
            searchWordSet.addAll(
                    Arrays.stream(replacedSearchTerm.split("/")).collect(Collectors.toList())
            );
        }
        return searchWordSet;
    }

    private List<NaverCategoryDto> readCategoryList() {
        try {
            List<NaverCategoryDto> categoryList = new ArrayList<>();
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(FIXED_FILE_PATH);
            Workbook workbook = new XSSFWorkbook(Objects.requireNonNull(inputStream));
            Sheet sheet = workbook.getSheetAt(FIXED_SHEET_INDEX);
            for (Row row : sheet) {
                if (row.getRowNum() == FIRST_ROW_INDEX) {
                    continue;
                }
                String[] cells = new String[FIXED_CELL_SIZE];
                for (int i = 0; i < FIXED_CELL_SIZE; ++i) {
                    Cell cell = row.getCell(i);
                    cells[i] = cell.getRichStringCellValue().getString();
                }
                NaverCategoryDto dto = NaverCategoryDto.createFrom(cells);
                categoryList.add(dto);
            }
            log.info("Read {} NAVER categories successfully.", categoryList.size());
            return categoryList;
        } catch (IOException ioException) {
            log.error("Error occured while reading NAVER categoryId excel file. Return empty list.", ioException);
            return Collections.emptyList();
        } catch (Exception exception) {
            log.error("Other Exception : msg : {}, cause : {}", exception.getMessage(), exception.getCause());
            return Collections.emptyList();
        }
    }

    private void lockIndicators() {
        this.running.set(true);
        this.startedAt = LocalDateTime.now();
    }

    private void unlockIndicators() {
        this.running.set(false);
    }

}

