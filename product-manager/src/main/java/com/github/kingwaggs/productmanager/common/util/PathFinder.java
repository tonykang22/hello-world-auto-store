package com.github.kingwaggs.productmanager.common.util;

import com.github.kingwaggs.productmanager.common.domain.FileType;
import com.github.kingwaggs.productmanager.common.domain.SelectScoreType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * For projectRootPath
 * - local env : "."
 * - dev or prod : "/root/hello-world-auto-store"
 * Do not include changes for root path to commit
 *
 * @Author waggs
 */
@Component
public class PathFinder {

    // READ COMMENT ON TOP OF THIS CLASS
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

    public static Path getDriversDir() {
        return Path.of(getProjectRootPath().toString(), "drivers");
    }

    public static Path getGeckodriver() {
        return Path.of(getDriversDir().toString(), "geckodriver");
    }

}
