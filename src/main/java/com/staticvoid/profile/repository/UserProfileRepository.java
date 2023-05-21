package com.staticvoid.profile.repository;

import com.staticvoid.profile.domain.UserProfile;
import com.staticvoid.user.domain.ApplicationUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findByUser(ApplicationUser user);

    @Query(value = "SELECT * FROM userprofile p INNER JOIN applicationuser u ON u.id = p.user_id" +
            " LEFT JOIN user_blocked_users bu on u.id = bu.user_id AND bu.blockedusers_id = ?3" +
            " WHERE LOWER(u.name) LIKE CONCAT('%', LOWER(?1), '%') " +
            "OR LOWER(u.email) LIKE CONCAT('%', LOWER(?1), '%') " +
            //This should prevent both blocked users, and users that have blocked the current user from showing up in the search results
            "AND (u.id NOT IN(?2) AND bu.user_id IS NULL) ORDER BY u.name ASC",
            countQuery = "SELECT COUNT(*) FROM userprofile p INNER JOIN applicationuser u ON u.id = p.user_id" +
                    " LEFT JOIN user_blocked_users bu on u.id = bu.user_id AND bu.blockedusers_id = ?3" +
                    " WHERE LOWER(u.name) LIKE CONCAT('%', LOWER(?1), '%')" +
                    " OR LOWER(u.email) LIKE CONCAT('%', LOWER(?1), '%')" +
                    //This should prevent both blocked users, and users that have blocked the current user from showing up in the search results
                    " AND (u.id NOT IN(?2) AND bu.user_id IS NULL)",
            nativeQuery = true)
    Page<UserProfile> search(String query, List<Long> blockedUsers, Long userId, Pageable pageable);
}
