package com.staticvoid.user.respository;

import com.staticvoid.user.domain.ApplicationUser;
import com.staticvoid.user.domain.ApplicationUserTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationUserTokensRepository extends JpaRepository<ApplicationUserTokens, Long> {
}
