package pl.pelipe.shoppinglist.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Long> {

    PasswordResetTokenEntity getByToken(String token);

    Integer deleteAllByExpiryDateBefore(LocalDateTime expiryDate);
}