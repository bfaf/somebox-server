package com.kchonov.someboxserver.repository;

import com.kchonov.someboxserver.models.MovieGanresEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieGanresEntityRepository extends JpaRepository<MovieGanresEntity, Integer> {
}