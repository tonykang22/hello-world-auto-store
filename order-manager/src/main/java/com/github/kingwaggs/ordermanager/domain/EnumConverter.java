package com.github.kingwaggs.ordermanager.domain;

import com.github.kingwaggs.ordermanager.domain.dto.SourceVendor;
import org.springframework.core.convert.converter.Converter;

public class EnumConverter {


    public static class StringToRunningStatus implements Converter<String, RunnerStatus> {

        @Override
        public RunnerStatus convert(String source) {
            return RunnerStatus.valueOf(source.toUpperCase());
        }
    }

    public static class StringToSourceVendor implements Converter<String, SourceVendor> {

        @Override
        public SourceVendor convert(String source) {
            return SourceVendor.valueOf(source.toUpperCase());
        }
    }

}
