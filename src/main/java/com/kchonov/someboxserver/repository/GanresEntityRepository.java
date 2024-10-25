package com.kchonov.someboxserver.repository;

import com.kchonov.someboxserver.models.GanresEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GanresEntityRepository extends JpaRepository<GanresEntity, Integer> {
}