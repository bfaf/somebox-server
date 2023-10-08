package com.kchonov.someboxserver.controllers.v1;

import com.kchonov.someboxserver.config.SomeBoxConfig;
import com.kchonov.someboxserver.entities.SomeBoxFileInfo;
import com.kchonov.someboxserver.services.FilesService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
public class ApiController {

    Logger logger = LoggerFactory.getLogger(ApiController.class);

    private final FilesService filesService;
    private final SomeBoxConfig someBoxConfig;

    public ApiController(FilesService filesService, SomeBoxConfig someBoxConfig) {
        this.filesService = filesService;
        this.someBoxConfig = someBoxConfig;
    }

    @GetMapping("/api/v1/list")
    public List<SomeBoxFileInfo> listFiles() {
        return filesService.listFiles();
    }

    /*
    @GetMapping(value = "/api/v1/play/{id}")
    @ResponseBody
    public ResponseEntity<StreamingResponseBody> playMediaV01(
            @PathVariable("id")
            Integer videoId,
            @RequestHeader(value = "Range", required = false)
            String rangeHeader,
            HttpServletResponse response)
    {
        return filesService.streamFile(videoId, rangeHeader, response);
    }
     */

    @GetMapping(value = "/api/v1/play/{id}")
    public void playMediaV01(
            @PathVariable("id")
            Integer videoId,
            HttpServletResponse response,
            HttpServletRequest request)
    {
        try {
            filesService.streamFile(videoId, request, response);
        } catch (Exception ex) {
            logger.error("Exception in API: ", ex);
        }
    }

    @GetMapping("/api/v1/image/{id}")
    public ResponseEntity<byte[]> getImageAsResponseEntity(@PathVariable("id") Integer imageId) throws IOException {
        return filesService.getImage(imageId);
    }
}
