package com.staticvoid.user.respository;

import com.staticvoid.user.domain.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("passwordResetTokenRepository")
public interface PasswordResetTokenRespository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByPasswordResetToken(String confirmationToken);
}
