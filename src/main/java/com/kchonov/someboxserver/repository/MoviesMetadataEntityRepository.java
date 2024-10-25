package com.kchonov.someboxserver.repository;

import com.kchonov.someboxserver.models.MoviesMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoviesMetadataEntityRepository extends JpaRepository<MoviesMetadataEntity, Integer> {
}