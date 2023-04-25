package com.staticvoid.search.repository;

import com.staticvoid.search.domain.Search;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SearchRepository extends JpaRepository<Search, Long> {

    @Query(value = "SELECT DISTINCT ON (query, type) * FROM search WHERE user_id=?1 ORDER BY query, type, date DESC LIMIT 10",
            nativeQuery = true)
    List<Search> findRecentSearchesByUserId(Long userId);
}
