package com.kchonov.someboxserver.services;

import com.kchonov.someboxserver.config.SomeBoxConfig;
import com.kchonov.someboxserver.entities.BasicSomeBoxFileInfo;
import com.kchonov.someboxserver.entities.SomeBoxFileInfo;
import com.kchonov.someboxserver.exceptions.VideoNotFoundException;
import com.kchonov.someboxserver.utilities.FileUtilities;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FilesService {

    Logger logger = LoggerFactory.getLogger(FilesService.class);

    private final Map<String, Boolean> FILE_FORMATS = Map.of(".mp4", true, ".mkv", true);

    private final SomeBoxConfig someBoxConfig;
    private final FileInfoService fileInfoService;

    private List<SomeBoxFileInfo> fileInfoList;

    private List<BasicSomeBoxFileInfo> basicList;

    private List<String> errorsToIgnore = List.of(
            "java.io.IOException: An established connection was aborted by the software in your host machine",
            "java.io.IOException: Connection reset by peer"
    );

    public FilesService(SomeBoxConfig someBoxConfig, FileInfoService fileInfoService) {
        this.someBoxConfig = someBoxConfig;
        this.fileInfoService = fileInfoService;
        this.fileInfoList = listFiles();
        this.basicList = listBasicFiles();
    }

    public List<SomeBoxFileInfo> listFiles() {
        if (this.fileInfoList != null) {
            return this.fileInfoList;
        }
        List<String> files = Stream.of(new File(someBoxConfig.sourceDir()).listFiles())
                .filter(file -> !file.isDirectory())
                .filter(file -> FILE_FORMATS.containsKey(FileUtilities.getExtension(file.getName())))
                .map(File::getName)
                .collect(Collectors.toList());

        this.fileInfoList = files.stream().map(file -> fileInfoService.GetFileInfo(file)).collect(Collectors.toList());
        for (int i = 0; i < this.fileInfoList.size(); i++) {
            this.fileInfoList.get(i).setId(Long.valueOf(i));
        }

        return this.fileInfoList;
    }

    public List<BasicSomeBoxFileInfo> listBasicFiles() {
        List<String> files = Stream.of(new File(someBoxConfig.sourceDir()).listFiles())
                .filter(file -> !file.isDirectory())
                .filter(file -> FILE_FORMATS.containsKey(FileUtilities.getExtension(file.getName())))
                .map(File::getName)
                .collect(Collectors.toList());

        List<BasicSomeBoxFileInfo> basicList = files.stream().map(file -> new BasicSomeBoxFileInfo(0L, file)).collect(Collectors.toList());
        for (int i = 0; i < basicList.size(); i++) {
            basicList.get(i).setId(Long.valueOf(i));
        }

        return basicList;
    }

    public ResponseEntity<byte[]> getImage(String movieFilename) throws IOException {
//        logger.info("Fetching image: " + movieFilename);
        if (this.fileInfoList == null) {
            listFiles();
        }
        List<SomeBoxFileInfo> filenameList = this.fileInfoList.stream().filter(f -> f.getFilename().compareTo(movieFilename) == 0
        ).collect(Collectors.toList());
//        logger.info("Images matching: " + filenameList.size());
        if (filenameList.size() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find requested video");
        }
        InputStream initialStream = null;

        try {
            Path path = Paths.get(someBoxConfig.screenshotDir(), filenameList.get(0).getScreenshotName());
//        logger.info("Fetching image: " + path.getFileName());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", MediaType.IMAGE_PNG_VALUE);
            initialStream = new FileInputStream(new File(path.toString()));
            byte[] media = IOUtils.toByteArray(initialStream);
            headers.setCacheControl(CacheControl.noCache().getHeaderValue());

            ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
            return responseEntity;
        } catch (IOException e) {
            logger.error("Something happened while generating image", e.getMessage());
            ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(null, null, HttpStatus.INTERNAL_SERVER_ERROR);
            return responseEntity;
        } finally {
            if (initialStream != null) {
                initialStream.close();
            }
        }
    }

    public void streamFile(String filename, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (this.fileInfoList == null) {
            listFiles();
        }
        // Minions 2015 720p BDRip x264 DUAL-SLS
        List<SomeBoxFileInfo> filenameList = this.fileInfoList.stream().filter(f -> f.getFilename().compareTo(filename) == 0).collect(Collectors.toList());
        if (filenameList.size() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find requested video");
        }
        Path path = Paths.get(someBoxConfig.sourceDir(), filenameList.get(0).getOriginalFilename());
//        logger.info("path to file: {}", path.toString());
        String filePathString = path.toString();
        final String mimeType = Files.probeContentType(path);
        final File movieFIle = new File(filePathString);
        final RandomAccessFile randomFile = new RandomAccessFile(movieFIle, "r");

        long rangeStart = 0;
        long rangeEnd = 0;
        boolean isPart = false;

        try {
            long movieSize = randomFile.length();
            String range = request.getHeader("range");
//             logger.info("range: {}", range);

            if (range != null) {
                if (range.endsWith("-")) {
                    range = range + (movieSize - 1);
                }
                int idxm = range.trim().indexOf("-");
                rangeStart = Long.parseLong(range.substring(6, idxm));
                rangeEnd = Long.parseLong(range.substring(idxm + 1));
                if (rangeStart > 0) {
                    isPart = true;
                }
            } else {
                rangeStart = 0;
                rangeEnd = movieSize - 1;
            }

            long partSize = rangeEnd - rangeStart + 1;
            // logger.debug("accepted range: {}", rangeStart + "-" + rangeEnd + "/" + partSize + " isPart:" + isPart);

            response.reset();
            response.setStatus(isPart ? 206 : 200);
            response.setContentType(mimeType);

            response.setHeader("Content-Range", "bytes " + rangeStart + "-" + rangeEnd + "/" + movieSize);
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Content-Length", "" + partSize);

            OutputStream out = response.getOutputStream();
            randomFile.seek(rangeStart);

            int bufferSize = 16 * 1024;
            byte[] buf = new byte[bufferSize];
            do {
                int block = partSize > bufferSize ? bufferSize : (int) partSize;
                int len = randomFile.read(buf, 0, block);
                out.write(buf, 0, len);
                partSize -= block;
            } while (partSize > 0);
            // logger.debug("sent " + movieFIle.getAbsolutePath() + " " + rangeStart + "-" + rangeEnd);
        } catch (IOException e) {
            if (!errorsToIgnore.contains(e.getMessage())) {
                logger.error("Transfer was aborted", e);
            }
        } finally {
            randomFile.close();
        }
    }
}
