package com.github.kingwaggs.productmanager.common.converter;

import com.github.kingwaggs.productmanager.common.domain.SelectScoreType;
import com.github.kingwaggs.productmanager.common.domain.SyncTarget;
import org.springframework.core.convert.converter.Converter;

public class EnumConverter {

    private EnumConverter() {}

    public static class StringToSyncTarget implements Converter<String, SyncTarget> {

        @Override
        public SyncTarget convert(String source) {
            return SyncTarget.valueOf(source.toUpperCase());
        }
    }

    public static class StringToSelectScoreType implements Converter<String, SelectScoreType> {

        @Override
        public SelectScoreType convert(String source) {
            return SelectScoreType.typeOf(source.toLowerCase());
        }
    }
}
