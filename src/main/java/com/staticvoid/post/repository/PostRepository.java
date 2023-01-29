package com.staticvoid.post.repository;

import com.staticvoid.image.domain.Image;
import com.staticvoid.post.domain.Post;
import com.staticvoid.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "SELECT * FROM Post WHERE user IN (?1) ORDER BY date ASC",
            countQuery = "SELECT COUNT(*) FROM Post WHERE user IN (?1)",
            nativeQuery = true)
    Page<Post> findByUsers(List<User> following, Pageable pageable);
}
