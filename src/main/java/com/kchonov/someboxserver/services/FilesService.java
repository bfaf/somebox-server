package com.kchonov.someboxserver.services;

import com.kchonov.someboxserver.config.SomeBoxConfig;
import com.kchonov.someboxserver.exceptions.ImageNotFoundException;
import com.kchonov.someboxserver.exceptions.VideoNotFoundException;
import com.kchonov.someboxserver.models.MoviesEntity;
import com.kchonov.someboxserver.repository.MoviesEntityRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FilesService {

    Logger logger = LoggerFactory.getLogger(FilesService.class);

    private final SomeBoxConfig someBoxConfig;

    private final MoviesEntityRepository moviesRepository;

    private List<String> errorsToIgnore = List.of(
            "java.io.IOException: An established connection was aborted by the software in your host machine",
            "java.io.IOException: Connection reset by peer"
    );

    public FilesService(SomeBoxConfig someBoxConfig, MoviesEntityRepository moviesRepository) {
        this.someBoxConfig = someBoxConfig;
        this.moviesRepository = moviesRepository;
    }


    /**
     * Fetch Base64 encoded image from database
     * @param movieId - movie to be found in DB
     * @return Returns Base64 encoded image
     * @throws ImageNotFoundException when movie is not found
     */
    public ResponseEntity<String> getImage(Integer movieId) {
       Optional<MoviesEntity> movie = moviesRepository.findByMovieId(movieId);
        if (!movie.isPresent()) {
            logger.error(String.format("Cannot find image for movie with id %d", movieId));
            throw new ImageNotFoundException("Cannot find image for the given movie");
        }

        return new ResponseEntity<>(movie.get().getMoviesMetadataEntity().getPoster(), HttpStatus.OK);
    }

    public void streamFile(Integer movieId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Optional<MoviesEntity> movie = moviesRepository.findByMovieId(movieId);
        if (!movie.isPresent()) {
            logger.error(String.format("Cannot find movie with id %d", movieId));
            throw new VideoNotFoundException("Cannot find movie with given id");
        }
        Path path = Paths.get(someBoxConfig.sourceDir(), movie.get().getFilename());
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
