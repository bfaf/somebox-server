package com.kchonov.someboxserver.services;

import com.kchonov.someboxserver.config.SomeBoxConfig;
import com.kchonov.someboxserver.entities.SomeBoxFileInfo;
import com.kchonov.someboxserver.utilities.FileUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileInfoService {

    Logger logger = LoggerFactory.getLogger(FileInfoService.class);

    private final SomeBoxConfig someBoxConfig;

    public FileInfoService(SomeBoxConfig someBoxConfig) {
        this.someBoxConfig = someBoxConfig;
    }

    public SomeBoxFileInfo GetFileInfo(String filename) {
        Path path = Paths.get(someBoxConfig.sourceDir(), filename);
        String strippedFilename = FileUtilities.stripFilename(filename);
        String screenshotFilename = FileUtilities.removeExtension(filename) + ".png";
        if (!FileUtilities.hasScreenshot(screenshotFilename)) {
            String screenshotPath = Paths.get(someBoxConfig.screenshotDir(), screenshotFilename).toString();
            FileUtilities.takeScreenshot(path.toString(), screenshotPath, someBoxConfig.screenshotWidth(), someBoxConfig.screenshotHeight());
        }
        // String screenshot = FileUtilities.hasScreenshot(screenshotFilename) ? screenshotFilename : "";

        return new SomeBoxFileInfo(
                0L, // will be populated by database
                strippedFilename,
                filename,
                screenshotFilename,
                FileUtilities.getFileDuration(path.toString()),
                FileUtilities.getFileDurationString(path.toString()),
                0L
        );
    }


}
