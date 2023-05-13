package com.staticvoid.user.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="token_id")
    private Long tokenId;

    @Column(name="confirmation_token")
    private String passwordResetToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;

    @OneToOne(targetEntity = ApplicationUser.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private ApplicationUser user;

    public PasswordResetToken(ApplicationUser user) {
        this.user = user;
        Date expiry = new Date();
        //adding 30 minutes to the created time
        expiryDate = Date.from(LocalDateTime.from(expiry.toInstant()).plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());
        passwordResetToken = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PasswordResetToken that = (PasswordResetToken) o;
        return Objects.equals(tokenId, that.tokenId) && Objects.equals(passwordResetToken, that.passwordResetToken) && Objects.equals(expiryDate, that.expiryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokenId, passwordResetToken, expiryDate);
    }
}
