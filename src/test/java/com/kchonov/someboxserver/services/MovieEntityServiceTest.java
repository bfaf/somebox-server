package com.kchonov.someboxserver.services;

import com.kchonov.someboxserver.models.MoviesEntity;
import com.kchonov.someboxserver.repository.MoviesEntityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovieEntityServiceTest {

    @Mock
    private MoviesEntityRepository moviesRepository;

    @InjectMocks
    private MovieEntityService movieEntityService;

    @Test
    void givenExistingMovieId_whenGetMovie_thenReturnMovie() {
        MoviesEntity savedMovie = new MoviesEntity("Test Movie", "2024", "test.mp4");

        when(moviesRepository.findByMovieId(1)).thenReturn(Optional.of(savedMovie));
        Optional<MoviesEntity> movie = movieEntityService.getMovie(1);
        assertTrue(movie.isPresent(), "Movie should be found");
        assertEquals("Test Movie", movie.get().getName(), "Movie id should match");
    }


}
