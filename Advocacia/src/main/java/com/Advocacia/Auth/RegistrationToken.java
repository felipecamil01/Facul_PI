package com.Advocacia.Auth;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Audited
public class RegistrationToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 128)
  private String token;

  @Column
  private String email;

  @Enumerated(EnumType.STRING)
  private UserRole role;

  private boolean used = false;

  private LocalDateTime createdAt = LocalDateTime.now();

  private LocalDateTime expiresAt;

  private LocalDateTime usedAt;

  private Long createdByUserId;

  private Long usedByUserId;
}
