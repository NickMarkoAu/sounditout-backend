package com.staticvoid.user.respository;

import com.staticvoid.user.domain.ApplicationUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {

    ApplicationUser findByEmail(String email);

    @Query(value = "SELECT * FROM applicationuser WHERE name LIKE CONCAT('%', ?1, '%') OR email LIKE CONCAT('%', ?1, '%') ORDER BY name ASC",
            countQuery = "SELECT COUNT(*) FROM applicationuser WHERE name LIKE CONCAT('%', ?1, '%') OR email LIKE CONCAT('%', ?1, '%')",
            nativeQuery = true)
    Page<ApplicationUser> search(String query, Pageable pageable);
}
