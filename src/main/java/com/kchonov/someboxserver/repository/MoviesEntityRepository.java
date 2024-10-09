package com.kchonov.someboxserver.repository;

import com.kchonov.someboxserver.models.MoviesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MoviesEntityRepository extends JpaRepository<MoviesEntity, Integer> {

    Optional<MoviesEntity> findByName(String name);
    Optional<MoviesEntity> findByMovieId(Integer Id);

    List<MoviesEntity> findAllByPublishedIsOrderByNameAsc(Short published);
}