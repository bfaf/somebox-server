package com.kchonov.someboxserver.repository;

import com.kchonov.someboxserver.models.MoviesEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MoviesEntityRepository extends JpaRepository<MoviesEntity, Integer> {

    @Cacheable("byName")
    Optional<MoviesEntity> findByName(String name);
    @Cacheable("byMovieId")
    Optional<MoviesEntity> findByMovieId(Integer Id);

    @Cacheable("publishedMovies")
    List<MoviesEntity> findAllByPublishedIsOrderByNameAsc(Short published);
}