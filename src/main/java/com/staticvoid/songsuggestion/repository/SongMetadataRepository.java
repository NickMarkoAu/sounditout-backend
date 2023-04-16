package com.staticvoid.songsuggestion.repository;

import com.staticvoid.songsuggestion.domain.SongMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongMetadataRepository extends JpaRepository<SongMetadata, Long> {
    SongMetadata findBySongId(Long songId);
}
