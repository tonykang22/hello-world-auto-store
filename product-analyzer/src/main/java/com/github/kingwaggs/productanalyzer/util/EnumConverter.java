package com.github.kingwaggs.productanalyzer.util;

import com.github.kingwaggs.productanalyzer.domain.SelectScoreType;
import org.springframework.core.convert.converter.Converter;

public class EnumConverter implements Converter<String, SelectScoreType> {

    @Override
    public SelectScoreType convert(String source) {
        return SelectScoreType.typeOf(source.toLowerCase());
    }
}
