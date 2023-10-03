package com.kchonov.someboxserver.controllers.v1;

import com.kchonov.someboxserver.config.SomeBoxConfig;
import com.kchonov.someboxserver.entities.SomeBoxFileInfo;
import com.kchonov.someboxserver.services.FilesService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
public class ApiController {

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

    @GetMapping(value = "/api/v1/play/{id}")
    @ResponseBody
    public ResponseEntity<StreamingResponseBody> playMediaV01(
            @PathVariable("id")
            Integer videoId,
            @RequestHeader(value = "Range", required = false)
            String rangeHeader)
    {
        return filesService.streamFile(videoId, rangeHeader);
    }
}
