package com.staticvoid.user.respository;

import com.staticvoid.user.domain.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, String> {
    ApplicationUser findByUsername(String username);
}
