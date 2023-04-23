package com.staticvoid.songsuggestion.repository;

import com.staticvoid.songsuggestion.domain.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByImageId(Long imageId);

    @Query(value = "SELECT * FROM song WHERE name LIKE CONCAT('%',?1,'%') OR tags LIKE CONCAT('%',?1,'%') OR artist LIKE CONCAT('%', ?1, '%') GROUP BY artist, name ORDER BY date DESC",
            countQuery = "SELECT COUNT(*) FROM song WHERE name LIKE CONCAT('%',?1,'%') OR tags LIKE CONCAT('%',?1,'%') OR artist LIKE CONCAT('%', ?1, '%') GROUP BY artist, name",
            nativeQuery = true)
    Page<Song> search(String query, Pageable pageable);
}
