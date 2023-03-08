package com.staticvoid.songsuggestion.repository;

import com.staticvoid.songsuggestion.domain.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByImageId(Long imageId);
}
