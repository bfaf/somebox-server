package com.kchonov.someboxserver.repository;

import com.kchonov.someboxserver.models.VideoStandardsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoStandardsEntityRepository extends JpaRepository<VideoStandardsEntity, Integer> {
}