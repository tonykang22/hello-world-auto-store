package com.github.kingwaggs.productanalyzerv2.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kingwaggs.productanalyzerv2.domain.FileType;
import com.github.kingwaggs.productanalyzerv2.domain.SelectScore;
import com.github.kingwaggs.productanalyzerv2.domain.SelectScoreType;
import com.github.kingwaggs.productanalyzerv2.domain.dto.ItemScoutIndicator;
import com.github.kingwaggs.productanalyzerv2.exception.UnreachableException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtil {


    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final Base64.Encoder ENCODER = Base64.getEncoder();
    private static final Base64.Decoder DECODER = Base64.getDecoder();
    private static final String SERIALIZE_DELIMITER = ".";
    private static final String DESERIALIZE_DELIMITER = "\\.";

    private static final String EXTENSION = "ser";
    private static final int INDEX_OF_DATE_IN_FILE_NAME = 0;

    public static Object readFile(FileType fileType, SelectScoreType selectScoreType, LocalDate date) throws IOException {
        String fileName = createFileName(date);
        Path filePath = PathFinder.getTargetFile(fileType, selectScoreType, fileName);

        String encodedJson = OBJECT_MAPPER.readValue(new File(filePath.toString()), String.class);

        byte[] base64Decoded = DECODER.decode(encodedJson);
        return readValue(fileType, new String(base64Decoded));
    }

    private static Object readValue(FileType fileType, String decodedJson) throws JsonProcessingException {
        switch (fileType) {
            case SELECT_SCORES:
                return OBJECT_MAPPER.readValue(decodedJson, new TypeReference<List<SelectScore>>() {});
            case INDICATOR:
                return OBJECT_MAPPER.readValue(decodedJson, new TypeReference<List<ItemScoutIndicator>>() {});
            default:
                throw new UnreachableException("Unreachable Statement.");
        }
    }

    @TrackExecutionTime
    public static void writeFile(FileType fileType, SelectScoreType selectScoreType, LocalDate date, Object object) throws IOException {
        String fileName = createFileName(date);
        Path filePath = PathFinder.getTargetFile(fileType, selectScoreType, fileName);

        String json = OBJECT_MAPPER.writeValueAsString(object);
        byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);
        String base64Encoded = ENCODER.encodeToString(jsonBytes);
        OBJECT_MAPPER.writeValue(new File(filePath.toString()), base64Encoded);
    }

    public static LocalDate findLatestDate(FileType fileType, SelectScoreType selectScoreType) throws FileNotFoundException {
        File directory = PathFinder.getTargetDir(fileType, selectScoreType).toFile();
        File[] files = directory.listFiles(File::isFile);
        if (files == null) {
            throw new FileNotFoundException(String.format("File does not exist in directory.(path : %s)", directory));
        }
        LocalDate latestDate = LocalDate.MIN;
        for (File file : files) {
            String[] split = file.getName().split(DESERIALIZE_DELIMITER);
            LocalDate date = DateTimeConverter.dateString2LocalDate(split[0]);
            if (date.isAfter(latestDate)) {
                latestDate = date;
            }
        }
        return latestDate;
    }

    public static void deleteFile(FileType fileType, SelectScoreType selectScoreType, LocalDate date) throws IOException {
        String fileName = createFileName(date);
        Path path = PathFinder.getTargetFile(fileType, selectScoreType, fileName);
        Files.deleteIfExists(path);
    }

    public static boolean isExist(FileType fileType, SelectScoreType selectScoreType, LocalDate date) {
        String fileName = createFileName(date);
        Path filePath = PathFinder.getTargetFile(fileType, selectScoreType, fileName);
        return Files.exists(filePath);
    }

    public static LocalDate parseDate(String fileName) {
        String[] split = fileName.split(DESERIALIZE_DELIMITER);
        return DateTimeConverter.dateString2LocalDate(split[INDEX_OF_DATE_IN_FILE_NAME]);
    }

    private static String createFileName(LocalDate targetDate) {
        StringBuilder stringBuilder = new StringBuilder();
        String dateString = DateTimeConverter.localDate2DateString(targetDate);
        return stringBuilder
                .append(dateString)
                .append(SERIALIZE_DELIMITER)
                .append(EXTENSION)
                .toString();
    }

}
