package com.staticvoid.userProfile.repository;

import com.staticvoid.userProfile.domain.UserProfile;
import com.staticvoid.user.domain.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findByUser(ApplicationUser user);
}
