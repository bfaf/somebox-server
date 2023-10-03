package com.kchonov.someboxserver.services;

import com.kchonov.someboxserver.config.SomeBoxConfig;
import com.kchonov.someboxserver.entities.SomeBoxFileInfo;
import com.kchonov.someboxserver.exceptions.VideoNotFoundException;
import com.kchonov.someboxserver.utilities.FileUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
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

    public FilesService(SomeBoxConfig someBoxConfig, FileInfoService fileInfoService) {
        this.someBoxConfig = someBoxConfig;
        this.fileInfoService = fileInfoService;
    }

    public List<SomeBoxFileInfo> listFiles() {
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

    public ResponseEntity<StreamingResponseBody> streamFile(int videoId, String rangeHeader) {
        if (this.fileInfoList == null) {
            listFiles();
        }
        if (videoId < 0 || videoId >= fileInfoList.size())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find video with id: " + videoId);
        }
        try
        {
            Path path = Paths.get(someBoxConfig.sourceDir(), this.fileInfoList.get(videoId).getOriginalFilename());
            StreamingResponseBody responseStream;
            String filePathString = path.toString();
            Path filePath = Paths.get(filePathString);
            Long fileSize = Files.size(filePath);
            byte[] buffer = new byte[1024];
            final HttpHeaders responseHeaders = new HttpHeaders();

            if (rangeHeader == null)
            {
                responseHeaders.add("Content-Type", "video/mp4");
                responseHeaders.add("Content-Length", fileSize.toString());
                responseStream = os -> {
                    RandomAccessFile file = new RandomAccessFile(filePathString, "r");
                    try (file)
                    {
                        long pos = 0;
                        file.seek(pos);
                        while (pos < fileSize - 1)
                        {
                            file.read(buffer);
                            os.write(buffer);
                            pos += buffer.length;
                        }
                        os.flush();
                    } catch (Exception e) {}
                };

                return new ResponseEntity<StreamingResponseBody>
                        (responseStream, responseHeaders, HttpStatus.OK);
            }

            String[] ranges = rangeHeader.split("-");
            Long rangeStart = Long.parseLong(ranges[0].substring(6));
            Long rangeEnd;
            if (ranges.length > 1)
            {
                rangeEnd = Long.parseLong(ranges[1]);
            }
            else
            {
                rangeEnd = fileSize - 1;
            }

            if (fileSize < rangeEnd)
            {
                rangeEnd = fileSize - 1;
            }

            String contentLength = String.valueOf((rangeEnd - rangeStart) + 1);
            responseHeaders.add("Content-Type", "video/mp4");
            responseHeaders.add("Content-Length", contentLength);
            responseHeaders.add("Accept-Ranges", "bytes");
            responseHeaders.add("Content-Range", "bytes" + " " +
                    rangeStart + "-" + rangeEnd + "/" + fileSize);
            final Long _rangeEnd = rangeEnd;
            responseStream = os -> {
                RandomAccessFile file = new RandomAccessFile(filePathString, "r");
                try (file)
                {
                    long pos = rangeStart;
                    file.seek(pos);
                    while (pos < _rangeEnd)
                    {
                        file.read(buffer);
                        os.write(buffer);
                        pos += buffer.length;
                    }
                    os.flush();
                }
                catch (Exception e) {}
            };

            return new ResponseEntity<StreamingResponseBody>
                    (responseStream, responseHeaders, HttpStatus.PARTIAL_CONTENT);
        }
        catch (FileNotFoundException e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (IOException e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
