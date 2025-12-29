package com.ibizabroker.lms.dao;

import com.ibizabroker.lms.entity.PasswordResetToken;
import com.ibizabroker.lms.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    void deleteAllByUser(Users user);
}
