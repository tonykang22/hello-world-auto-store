package com.github.kingwaggs.productanalyzer.service;

import com.github.kingwaggs.productanalyzer.domain.entity.ForbiddenIngredient;
import com.github.kingwaggs.productanalyzer.repository.ForbiddenIngredientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForbiddenIngredientService {

    private final ForbiddenIngredientRepository repository;

    public List<String> getForbiddenIngredients() {
        log.info("Get forbidden ingredients.");
        return repository.findAll()
                .stream()
                .map(ForbiddenIngredient::getName)
                .collect(Collectors.toList());
    }

}
