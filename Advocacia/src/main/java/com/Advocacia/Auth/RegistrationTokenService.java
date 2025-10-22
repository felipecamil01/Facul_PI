package com.Advocacia.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RegistrationTokenService {

  private static final int DEFAULT_VALIDITY_HOURS = 168; // 7 dias

  @Autowired
  private RegistrationTokenRepository registrationTokenRepository;

  @Autowired
  private JavaMailSender mailSender;

  @Value("${app.registration-token.sender:noreply@lionlaw.app}")
  private String defaultSender;

  private final SecureRandom secureRandom = new SecureRandom();

  public RegistrationToken generateToken(String email, UserRole role, Integer validadeHoras, Long createdByUserId) {
    RegistrationToken token = new RegistrationToken();
    token.setToken(generateRandomToken());
    token.setEmail(email);
    token.setRole(role != null ? role : UserRole.SECRETARIA);
    token.setCreatedByUserId(createdByUserId);

    LocalDateTime expiresAt = LocalDateTime.now().plusHours(
      validadeHoras != null && validadeHoras > 0 ? validadeHoras : DEFAULT_VALIDITY_HOURS
    );
    token.setExpiresAt(expiresAt);

    RegistrationToken saved = registrationTokenRepository.save(token);
    if (email != null && !email.isBlank()) {
      enviarEmailToken(saved);
    }
    return saved;
  }

  private String generateRandomToken() {
    byte[] bytes = new byte[48]; // 48 bytes -> 64 caracteres Base64 URL Safe
    secureRandom.nextBytes(bytes);
    return java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }

  private void enviarEmailToken(RegistrationToken token) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(token.getEmail());
    message.setSubject("Token de registro - LionLaw");
    message.setText("""
        Olá,

        Utilize o token abaixo para concluir o seu cadastro no sistema LionLaw:

        %s

        Este token expira em: %s

        Caso não tenha solicitado este acesso, desconsidere este e-mail.

        Atenciosamente,
        Equipe LionLaw
        """.formatted(token.getToken(), token.getExpiresAt()));
    try {
      mailSender.send(message);
    } catch (Exception ignored) {
      // Evita quebrar fluxo caso o servidor de e-mail esteja indisponível.
    }
  }

  public RegistrationToken validarTokenDisponivel(String token, String email) {
    RegistrationToken registrationToken = registrationTokenRepository.findByToken(token)
      .orElseThrow(() -> new IllegalArgumentException("Token de registro inválido."));

    if (registrationToken.isUsed()) {
      throw new IllegalArgumentException("Token de registro já utilizado.");
    }

    if (registrationToken.getExpiresAt() != null && registrationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new IllegalArgumentException("Token de registro expirado.");
    }

    if (registrationToken.getEmail() != null && email != null
        && !registrationToken.getEmail().equalsIgnoreCase(email)) {
      throw new IllegalArgumentException("Token de registro não corresponde ao e-mail informado.");
    }

    return registrationToken;
  }

  @Transactional
  public void marcarComoUsado(RegistrationToken token, Long usuarioId) {
    token.setUsed(true);
    token.setUsedAt(LocalDateTime.now());
    token.setUsedByUserId(usuarioId);
    registrationTokenRepository.save(token);
  }

  public Optional<RegistrationToken> buscarPorToken(String token) {
    return registrationTokenRepository.findByToken(token);
  }
}
