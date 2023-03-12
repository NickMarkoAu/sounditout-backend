package com.staticvoid.post.repository;

import com.staticvoid.post.domain.Post;
import com.staticvoid.user.domain.ApplicationUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "SELECT * FROM post WHERE user_id IN (?1) AND (privacy=0 OR privacy=1) ORDER BY date ASC",
            countQuery = "SELECT COUNT(*) FROM Post WHERE user IN (?1) AND (privacy LIKE 0 OR privacy LIKE 1)",
            nativeQuery = true)
    Page<Post> findByUsers(List<Long> following, Pageable pageable);
}
