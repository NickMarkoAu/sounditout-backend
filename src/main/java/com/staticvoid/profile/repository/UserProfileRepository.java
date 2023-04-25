package com.staticvoid.profile.repository;

import com.staticvoid.profile.domain.UserProfile;
import com.staticvoid.user.domain.ApplicationUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findByUser(ApplicationUser user);

    @Query(value = "SELECT * FROM userprofile p INNER JOIN applicationuser u ON u.id = p.user_id WHERE LOWER(u.name) LIKE CONCAT('%', LOWER(?1), '%') OR LOWER(u.email) LIKE CONCAT('%', LOWER(?1), '%') ORDER BY u.name ASC",
            countQuery = "SELECT COUNT(*) FROM userprofile p INNER JOIN applicationuser u ON u.id = p.user_id WHERE LOWER(u.name) LIKE CONCAT('%', LOWER(?1), '%') OR LOWER(u.email) LIKE CONCAT('%', LOWER(?1), '%')",
            nativeQuery = true)
    Page<UserProfile> search(String query, Pageable pageable);
}
