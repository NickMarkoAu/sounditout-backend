package com.staticvoid.search.repository;

import com.staticvoid.search.domain.Search;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SearchRepository extends JpaRepository<Search, Long> {

    @Query(value = "SELECT * FROM search WHERE user_id=?1 ORDER BY date DESC LIMIT 10",
            nativeQuery = true)
    List<Search> findRecentSearchesByUserId(Long userId);
}
