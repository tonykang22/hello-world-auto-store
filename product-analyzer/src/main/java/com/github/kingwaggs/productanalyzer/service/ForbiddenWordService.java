package com.github.kingwaggs.productanalyzer.service;

import com.github.kingwaggs.productanalyzer.domain.ForbiddenWord;
import com.github.kingwaggs.productanalyzer.repository.ForbiddenWordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForbiddenWordService {

    private final ForbiddenWordRepository forbiddenWordRepository;

    public List<ForbiddenWord> getAllForbiddenWords() {
        return forbiddenWordRepository.findAll();
    }

    public List<ForbiddenWord> saveForbiddenWords(List<String> forbiddenWordList) {
        List<ForbiddenWord> entityList = forbiddenWordList.stream()
                .filter(word -> !forbiddenWordRepository.existsByWord(word))
                .map(ForbiddenWord::create)
                .collect(Collectors.toList());
        return forbiddenWordRepository.saveAll(entityList);
    }

    public void deleteForbiddenWords(List<String> forbiddenWordList) {
        List<ForbiddenWord> entityList = forbiddenWordList.stream()
                .map(forbiddenWordRepository::findByWord)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        forbiddenWordRepository.deleteAll(entityList);
    }
}
