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

    @Query(value = "SELECT * FROM post WHERE user_id IN (?1) AND (privacy=0 OR privacy=2) ORDER BY date DESC",
            countQuery = "SELECT COUNT(*) FROM post WHERE user_id IN (?1) AND (privacy=0 OR privacy=2)",
            nativeQuery = true)
    Page<Post> findByUsers(List<Long> following, Pageable pageable);

    @Query(value = "SELECT * FROM post WHERE user_id=?1 AND (privacy=0 OR privacy=2) ORDER BY date DESC",
            countQuery = "SELECT COUNT(*) FROM post WHERE user_id=?1 AND (privacy=0 OR privacy=2)",
            nativeQuery = true)
    Page<Post> findByUser(ApplicationUser user, Pageable pageable);

    Page<Post> findAllByUser(ApplicationUser user, Pageable pageable);

    Long countAllByUser(ApplicationUser user);

    //TODO add privacy, will need to join to see if user is following
    @Query(value = "SELECT * FROM post WHERE LOWER(content) LIKE CONCAT('%',LOWER(?1),'%') OR LOWER(tags) LIKE CONCAT('%',LOWER(?1),'%') ORDER BY date DESC",
            countQuery = "SELECT COUNT(*) FROM post WHERE LOWER(content) LIKE CONCAT('%',LOWER(?1),'%') OR LOWER(tags) LIKE CONCAT('%',LOWER(?1),'%')",
            nativeQuery = true)
    Page<Post> search(String query, Pageable pageable);
}
