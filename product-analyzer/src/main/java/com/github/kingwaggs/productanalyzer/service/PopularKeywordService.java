package com.github.kingwaggs.productanalyzer.service;

import com.github.kingwaggs.productanalyzer.domain.SourcingContext;
import com.github.kingwaggs.productanalyzer.domain.dto.request.PopularKeywordRequestDto;
import com.github.kingwaggs.productanalyzer.domain.entity.PopularKeyword;
import com.github.kingwaggs.productanalyzer.repository.PopularKeywordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PopularKeywordService {

    private final PopularKeywordRepository repository;
    private final SourcingContext sourcingContext;

    public List<PopularKeyword> getTargetPopularKeywords() {
        long cycle = sourcingContext.getPopularKeywordCycle();
        log.info("Getting targeted popular keywords start. The keywords which has not been used for {} days", cycle);
        LocalDateTime unusedDays = LocalDateTime.now().minusDays(cycle);
        List<PopularKeyword> result = repository.findByLastSourcingAtBefore(unusedDays);
        result.forEach(word -> word.updateLastSourcingAt(LocalDateTime.now()));
        log.info("Getting targeted popular keywords complete. Result : {}", result);
        return result;
    }

    public List<PopularKeyword> getAllPopularKeywords() {
        return repository.findAll();
    }

    public List<PopularKeyword> savePopularKeywords(List<PopularKeywordRequestDto.PopularKeywordDto> popularKeywordList) {
        List<PopularKeyword> entityList = popularKeywordList.stream()
                .filter(dto -> !repository.existsByKeyword(dto.getKeyword()))
                .map(dto -> PopularKeyword.create(dto.getKeyword(), dto.getProductType()))
                .collect(Collectors.toList());
        return repository.saveAll(entityList);
    }

    public void deletePopularKeywords(List<String> popularKeyWordList) {
        List<PopularKeyword> entityList = popularKeyWordList.stream()
                .map(repository::findByKeyword)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        repository.deleteAll(entityList);
    }

}
