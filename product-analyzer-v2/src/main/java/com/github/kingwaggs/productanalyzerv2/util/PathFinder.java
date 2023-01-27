package com.github.kingwaggs.productanalyzerv2.util;

import com.github.kingwaggs.productanalyzerv2.domain.FileType;
import com.github.kingwaggs.productanalyzerv2.domain.SelectScoreType;
import org.springframework.beans.factory.annotation.Value;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathFinder {

    private static String rootPath;

    @Value("${root.path}")
    public void initRootPath(String path) {
        PathFinder.rootPath = path;
    }

    private static Path getProjectRootPath() {
        return Paths.get(rootPath).normalize().toAbsolutePath();
    }

    public static Path getOutputFileDir() {
        return Path.of(getProjectRootPath().toString(), "output-file");
    }

    public static Path getTargetDir(FileType fileType, SelectScoreType selectScoreType) {
        return Path.of(getOutputFileDir().toString(), fileType.getType(), selectScoreType.getType());
    }

    public static Path getTargetFile(FileType fileType, SelectScoreType selectScoreType, String fileName) {
        return Path.of(getTargetDir(fileType, selectScoreType).toString(), fileName);
    }

}
