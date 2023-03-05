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

    @Query(value = "SELECT * FROM Post WHERE user IN (?1) AND privacy LIKE 'FRIENDS' ORDER BY date ASC",
            countQuery = "SELECT COUNT(*) FROM Post WHERE user IN (?1) AND privacy LIKE 'FRIENDS'",
            nativeQuery = true)
    Page<Post> findByUsers(List<ApplicationUser> following, Pageable pageable);
}
