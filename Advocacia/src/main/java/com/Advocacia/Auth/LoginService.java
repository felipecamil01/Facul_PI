package com.Advocacia.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.Advocacia.Config.JwtServiceGenerator;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

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

  public String logar(Login login) {
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        login.getUsername(),
        login.getPassword()
      )
    );

    Usuario user = loginRepository.findByUsername(login.getUsername())
      .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    return jwtService.generateToken(user);
  }

  public void gerarTokenRecuperacao(String email) {
    Usuario usuario = usuarioRepository.findByEmail(email)
      .orElseThrow(() -> new RuntimeException("Email não encontrado"));

    String token = gerarTokenAleatorio();

    PasswordResetToken passwordResetToken = new PasswordResetToken();
    passwordResetToken.setToken(token);
    passwordResetToken.setUsuario(usuario);
    passwordResetToken.setExpiracaoData(new Date(System.currentTimeMillis() + (60 * 60 * 1000)));

    passwordResetTokenRepository.save(passwordResetToken);
    enviarEmailRecuperacao(email, token);
  }

  private String gerarTokenAleatorio() {
    int numeroAleatorio = (int) (Math.random() * 900000) + 100000;
    return String.valueOf(numeroAleatorio);
  }


  private void enviarEmailRecuperacao(String email, String token) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(email);
    message.setSubject("Recuperação de Senha");
    message.setText("Seu token de recuperação é: " + token);
    mailSender.send(message);
  }

  public boolean validarTokenRecuperacao(String email, String token) {
    Optional<PasswordResetToken> tokenRecuperacaoOpt = passwordResetTokenRepository.findByToken(token);
    if (tokenRecuperacaoOpt.isPresent()) {
      PasswordResetToken tokenRecuperacao = tokenRecuperacaoOpt.get();

      boolean emailValido = tokenRecuperacao.getUsuario().getEmail().equalsIgnoreCase(email);
      boolean tokenValido = tokenRecuperacao.getExpiracaoData().toInstant().isAfter(Instant.now());

      return emailValido && tokenValido;
    }

    return false;
  }

  public void redefinirSenha(String email, String token, String novaSenha) {
    if (validarTokenRecuperacao(email, token)) {
      Usuario usuario = usuarioRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Email não encontrado"));

      usuario.setPassword(passwordEncoder.encode(novaSenha));
      usuarioRepository.save(usuario);

      passwordResetTokenRepository.deleteByToken(token);
    } else {
      throw new RuntimeException("Token inválido ou expirado");
    }
  }
}
