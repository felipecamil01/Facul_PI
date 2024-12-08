package com.Advocacia.Config;

import java.security.Key;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class JwtPasswordRecoveryService {

  private static final Logger logger = LoggerFactory.getLogger(JwtPasswordRecoveryService.class);

  // Tempo de expiração do token (1 hora)
  private static final long PASSWORD_RECOVERY_EXPIRATION = 60 * 60 * 1000;

  // Gera o token JWT de recuperação de senha
  public String generatePasswordRecoveryToken(String email) {
    Map<String, Object> extraClaims = new HashMap<>();
    extraClaims.put("email", email);
    extraClaims.put("type", "password_recovery");

    return Jwts.builder()
      .setClaims(extraClaims)
      .setSubject(email)
      .setIssuedAt(new Date(System.currentTimeMillis()))
      .setExpiration(new Date(System.currentTimeMillis() + PASSWORD_RECOVERY_EXPIRATION))
      .signWith(getSigningKey(), JwtConfig.ALGORITMO_ASSINATURA)
      .compact();
  }

  // Valida o token JWT
  public boolean validatePasswordRecoveryToken(String token) {
    try {
      Claims claims = extractAllClaims(token);
      logger.info("Token validado com sucesso para o email: {}", claims.getSubject());
      return "password_recovery".equals(claims.get("type")) && !claims.getExpiration().before(new Date());
    } catch (ExpiredJwtException e) {
      logger.warn("Token expirado", e);
      return false;
    } catch (SignatureException e) {
      logger.warn("Assinatura do token inválida", e);
      return false;
    } catch (Exception e) {
      logger.warn("Erro ao validar token", e);
      return false;
    }

  }

  // Extrai o e-mail do token JWT
  public String extractEmailFromPasswordRecoveryToken(String token) {
    Claims claims = extractAllClaims(token);
    return claims.getSubject();
  }

  // Valida e extrai o e-mail
  public String validateAndExtractEmail(String token) {
    if (validatePasswordRecoveryToken(token)) {
      return extractEmailFromPasswordRecoveryToken(token);
    }
    throw new IllegalArgumentException("Token inválido ou expirado");
  }

  // Gera um novo token de recuperação de senha
  public String refreshPasswordRecoveryToken(String token) {
    if (validatePasswordRecoveryToken(token)) {
      String email = extractEmailFromPasswordRecoveryToken(token);
      return generatePasswordRecoveryToken(email);
    }
    throw new IllegalArgumentException("Token inválido ou expirado");
  }

  // Gera o hash do token
  public String hashToken(String token) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] encodedHash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(encodedHash);
    } catch (Exception e) {
      throw new RuntimeException("Erro ao gerar hash do token", e);
    }
  }

  private Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(JwtConfig.SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  // Extrai todas as claims do token JWT
  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(getSigningKey())
      .build()
      .parseClaimsJws(token)
      .getBody();
  }
}
