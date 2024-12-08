package com.Advocacia.Auth;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Advocacia.Config.JwtPasswordRecoveryService;
import com.Advocacia.Config.JwtServiceGenerator;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class LoginService {

  @Autowired
  private LoginRepository loginRepository;
  @Autowired
  private JwtServiceGenerator jwtService;
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private UsuarioRepository usuarioRepository;
  @Autowired
  private PasswordResetTokenRepository passwordResetTokenRepository; // Repositório do token de recuperação
  @Autowired
  private JavaMailSender mailSender;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private JwtPasswordRecoveryService jwtPasswordRecoveryService;

  public String logar(Login login) {
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        login.getUsername(),
        login.getPassword()
      )
    );

    Usuario user = loginRepository.findByUsername(login.getUsername())
      .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    String jwtToken = jwtService.generateToken(user);
    return jwtToken;
  }

//recuperar senha
public void gerarTokenRecuperacao(String email) {
  Usuario usuario = usuarioRepository.findByEmail(email)
    .orElseThrow(() -> new RuntimeException("Email não encontrado"));

  String token = jwtPasswordRecoveryService.generatePasswordRecoveryToken(email);
  String tokenHash = jwtPasswordRecoveryService.hashToken(token);

  PasswordResetToken passwordResetToken = new PasswordResetToken();
  passwordResetToken.setToken(tokenHash);
  passwordResetToken.setUsuario(usuario);
  passwordResetToken.setExpiracaoData(new Date(System.currentTimeMillis() + (60 * 60 * 1000)));

  passwordResetTokenRepository.save(passwordResetToken);

  enviarEmailRecuperacao(email, token);
}

  private void enviarEmailRecuperacao(String email, String token) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(email);
    message.setSubject("Recuperação de Senha");
    message.setText("Seu token de recuperação é: " + token);
    mailSender.send(message);
  }


  public boolean validarTokenRecuperacao(String email, String token) {
    // Buscando o token de recuperação no banco
    Optional<PasswordResetToken> tokenRecuperacaoOpt = passwordResetTokenRepository.findByToken(token);

    if (tokenRecuperacaoOpt.isPresent()) {
      PasswordResetToken tokenRecuperacao = tokenRecuperacaoOpt.get();
      // Verificando se o token corresponde ao email e se não está expirado
      return tokenRecuperacao.getUsuario().getEmail().equals(email) &&
        LocalDateTime.now().isBefore(tokenRecuperacao.getExpiracaoData().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
    }

    return false; // Token não encontrado ou inválido
  }

  public void redefinirSenha(String email, String token, String novaSenha) {
    if (validarTokenRecuperacao(email, token)) {
      Usuario usuario = usuarioRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Email não encontrado"));

      usuario.setPassword(passwordEncoder.encode(novaSenha));
      usuarioRepository.save(usuario);

      // Remover o token após a redefinição da senha
      passwordResetTokenRepository.deleteByToken(token);
    } else {
      throw new RuntimeException("Token inválido ou expirado");
    }
  }

  private String generateRandomToken() {
    return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
  }
}
