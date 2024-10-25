package com.kchonov.someboxserver.services;

import com.kchonov.someboxserver.models.MoviesEntity;
import com.kchonov.someboxserver.repository.MoviesEntityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieEntityService {

    private final MoviesEntityRepository moviesRepository;

    public MovieEntityService(MoviesEntityRepository moviesRepository) {
        this.moviesRepository = moviesRepository;
    }

    public Optional<MoviesEntity> getMovie(Integer id) {
        return this.moviesRepository.findByMovieId(id);
    }

    public List<MoviesEntity> getAllPublished() {
        return moviesRepository.findAllByPublishedIsOrderByNameAsc((short)1);
    }
}
