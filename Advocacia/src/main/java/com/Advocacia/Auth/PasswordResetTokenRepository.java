package com.Advocacia.Auth;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

  @Transactional
  @Modifying
  @Query("DELETE FROM PasswordResetToken t WHERE t.token = :token")
  void deleteByToken(@Param("token") String token);

  Optional<PasswordResetToken> findByToken(String token);
}
