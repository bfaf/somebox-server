package com.kchonov.someboxserver.repository;

import com.kchonov.someboxserver.models.MovieTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieTypeEntityRepository extends JpaRepository<MovieTypeEntity, Integer> {
}