package com.kchonov.someboxserver.utilities;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileUtilities {

    static Logger logger = LoggerFactory.getLogger(FileUtilities.class);

    public static String stripFilename(String filename) {
        return removeExtension(filename).replaceAll("\\.", " ");
    }

    public static String removeExtension(String filename) {
        int idx = filename.lastIndexOf(".");
        if (idx == -1)
            return filename;
        return filename.substring(0, idx);
    }

    public static String getExtension(String filename) {
        int idx = filename.lastIndexOf(".");
        if (idx == -1)
            return filename;

        return filename.substring(idx);
    }

    public static Long getFileDuration(String pathToFile) {
        Long duration = 0L;
        FFmpegFrameGrabber grabber;
        try {
            grabber = new FFmpegFrameGrabber(pathToFile);
            grabber.start();
            duration = grabber.getLengthInTime() / 1000;
            grabber.stop();
        } catch (FFmpegFrameGrabber.Exception ex) {
            logger.error("IOException occurred while fetching info for video: " + pathToFile, ex);
        }
        // logger.debug(String.format("Duration %s for video: %s " + duration.toString(), pathToFile));

        return duration;
    }

    public static String getFileDurationString(String pathToFile) {
        Long duration = getFileDuration(pathToFile);
        String dur = DurationFormatUtils.formatDuration(duration, "HH:mm:ss", true);
        // logger.debug(String.format("String duration %s for video: %s " + dur, pathToFile));
        return dur;
    }

    public static void takeScreenshot(String pathToFile, String outputFile, int width, int height) {
        FFmpegFrameGrabber grabber;
        try {
            grabber = new FFmpegFrameGrabber(pathToFile);
            grabber.start();
            grabber.setImageWidth(width);
            grabber.setImageHeight(height);
            int totalFrames = grabber.getLengthInFrames();
            grabber.setFrameNumber(totalFrames / 2);

            Java2DFrameConverter frameConverter = new Java2DFrameConverter();
            BufferedImage screenshot = frameConverter.convert(grabber.grabImage());
            ImageIO.write(screenshot, "png", new File(outputFile));

            grabber.stop();
        } catch (FFmpegFrameGrabber.Exception ex) {
            logger.error("FFmpegFrameGrabber.Exception occurred while fetching info for video: " + pathToFile, ex);
        } catch(IOException ex) {
            logger.error("IOException occurred while taking screenshot for video: " + pathToFile, ex);
        }
    }

    public static boolean hasScreenshot(String pathToFile) {
        return new File(pathToFile).exists();
    }
}
