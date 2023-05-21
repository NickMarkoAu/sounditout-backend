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
    @Query(value = "SELECT * " +
            "FROM post p " +
            "INNER JOIN applicationuser u ON p.user_id = u.id " +
            "LEFT JOIN user_blocked_users bu ON u.id = bu.user_id AND bu.blockedusers_id = ?3 " +
            "WHERE (" +
            "    LOWER(p.content) LIKE CONCAT('%', LOWER(?1), '%') " +
            "    OR LOWER(p.tags) LIKE CONCAT('%', LOWER(?1), '%') " +
            "    OR LOWER(u.name) LIKE CONCAT('%', LOWER(?1), '%')" +
            ") " +
            "AND (u.id NOT IN (?2) AND bu.user_id IS NULL)" +
            "ORDER BY p.date DESC",
            countQuery = "SELECT COUNT(*) " +
                    "FROM post p " +
                    "INNER JOIN applicationuser u ON p.user_id = u.id " +
                    "LEFT JOIN user_blocked_users bu ON u.id = bu.user_id AND bu.blockedusers_id = ?3 " +
                    "WHERE (" +
                    "    LOWER(p.content) LIKE CONCAT('%', LOWER(?1), '%') " +
                    "    OR LOWER(p.tags) LIKE CONCAT('%', LOWER(?1), '%') " +
                    "    OR LOWER(u.name) LIKE CONCAT('%', LOWER(?1), '%')" +
                    ") " +
                    "AND (u.id NOT IN (?2) AND bu.user_id IS NULL)",
            nativeQuery = true)
    Page<Post> search(String query, List<Long> blockedUsers, Pageable pageable);
}
