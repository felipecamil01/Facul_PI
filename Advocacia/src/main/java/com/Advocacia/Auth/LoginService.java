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

import java.time.Instant;
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
  private PasswordResetTokenRepository passwordResetTokenRepository;
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

  // Recuperação de senha
  public void gerarTokenRecuperacao(String email) {
    Usuario usuario = usuarioRepository.findByEmail(email)
      .orElseThrow(() -> new RuntimeException("Email não encontrado"));

    // Geração do token JWT
    String token = jwtPasswordRecoveryService.generatePasswordRecoveryToken(email);

    // Criação e armazenamento do token de recuperação (armazena o token original, não criptografado)
    PasswordResetToken passwordResetToken = new PasswordResetToken();
    passwordResetToken.setToken(token); // Armazenando o token original
    passwordResetToken.setUsuario(usuario);
    passwordResetToken.setExpiracaoData(new Date(System.currentTimeMillis() + (60 * 60 * 1000))); // Token expira em 1 hora

    passwordResetTokenRepository.save(passwordResetToken);

    // Envio do e-mail de recuperação
    enviarEmailRecuperacao(email, token);
  }


  // Método de envio do e-mail
  private void enviarEmailRecuperacao(String email, String token) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(email);
    message.setSubject("Recuperação de Senha");
    message.setText("Seu token de recuperação é: " + token);
    mailSender.send(message);
  }

  // Validação do token de recuperação
  public boolean validarTokenRecuperacao(String email, String token) {
    // Busca pelo token de recuperação no banco de dados
    Optional<PasswordResetToken> tokenRecuperacaoOpt = passwordResetTokenRepository.findByToken(token);
    System.out.println(tokenRecuperacaoOpt);
    if (tokenRecuperacaoOpt.isPresent()) {
      PasswordResetToken tokenRecuperacao = tokenRecuperacaoOpt.get();

      // Verifica se o token corresponde ao email (ignorando maiúsculas/minúsculas)
      boolean emailValido = tokenRecuperacao.getUsuario().getEmail().equalsIgnoreCase(email);
      System.out.println("Email fornecido: " + email);
      System.out.println("Email do usuário associado ao token: " + tokenRecuperacao.getUsuario().getEmail());
      System.out.println("Email válido: " + emailValido);

      // Verifica se o token não está expirado
      boolean tokenValido = tokenRecuperacao.getExpiracaoData().toInstant().isAfter(Instant.now());
      System.out.println("Data de expiração do token: " + tokenRecuperacao.getExpiracaoData());
      System.out.println("Data atual: " + Instant.now());
      System.out.println("Token válido (não expirado): " + tokenValido);

      if (!emailValido) {
        System.out.println("Falha: O email não corresponde ao email associado ao token.");
      }

      if (!tokenValido) {
        System.out.println("Falha: O token está expirado.");
      }

      return emailValido && tokenValido;
    } else {
      System.out.println("Falha: O token não foi encontrado no banco de dados.");
    }

    return false; // Token não encontrado ou inválido
  }

  // Redefinir senha
  public void redefinirSenha(String email, String token, String novaSenha) {
    if (validarTokenRecuperacao(email, token)) {
      Usuario usuario = usuarioRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Email não encontrado"));

      // Atualiza a senha do usuário
      usuario.setPassword(passwordEncoder.encode(novaSenha));
      usuarioRepository.save(usuario);

      // Remove o token após a redefinição da senha
      passwordResetTokenRepository.deleteByToken(token);
    } else {
      throw new RuntimeException("Token inválido ou expirado");
    }

  }

  private String generateRandomToken() {
    return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
  }
}
