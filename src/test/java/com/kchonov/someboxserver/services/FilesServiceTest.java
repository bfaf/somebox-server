package com.kchonov.someboxserver.services;

import com.kchonov.someboxserver.exceptions.ImageNotFoundException;
import com.kchonov.someboxserver.models.MoviesEntity;
import com.kchonov.someboxserver.models.MoviesMetadataEntity;
import com.kchonov.someboxserver.repository.MoviesEntityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FilesServiceTest {

    @Mock
    private MoviesEntityRepository moviesRepository;

    @InjectMocks
    private FilesService fileService;

    @Test
    void givenMovieId_returnImage() {
        MoviesEntity savedMovie = new MoviesEntity("Test Movie", "2024", "test.mp4");
        MoviesMetadataEntity movieMetadataEntity = new MoviesMetadataEntity();
        movieMetadataEntity.setPoster("poster");
        savedMovie.setMoviesMetadataEntity(movieMetadataEntity);

        when(moviesRepository.findByMovieId(1)).thenReturn(Optional.of(savedMovie));
        ResponseEntity<String> image = fileService.getImage(1);
        assertEquals("poster", image.getBody(), "Base64 image should be returned");
    }

    @Test
    void givenNonExistingMovieId_ThrowAnError() {
        Exception exception = assertThrows(ImageNotFoundException.class, () -> {
            fileService.getImage(10);
        });

        String expectedMessage = "Cannot find image for the given movie";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
