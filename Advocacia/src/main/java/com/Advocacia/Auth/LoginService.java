
package com.Advocacia.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.Advocacia.Config.JwtServiceGenerator;

import java.util.UUID;

@Service
public class LoginService {

	@Autowired
	private LoginRepository repository;
	@Autowired
	private JwtServiceGenerator jwtService;
	@Autowired
	private AuthenticationManager authenticationManager;
@Autowired
private JavaMailSender javaMailSender ;

	public String logar(Login login) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						login.getUsername(),
						login.getPassword()
						)
				);
		Usuario user = repository.findByUsername(login.getUsername()).get();
		String jwtToken = jwtService.generateToken(user);

		return jwtToken;
	}
  public void gerarTokenRecuperacaoSenha(String email) {
    // Gerar token de recuperação
    String token = UUID.randomUUID().toString();

    // Enviar email
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(email);
    message.setSubject("Recuperação de Senha");
    message.setText("Seu token de recuperação: " + token);
    javaMailSender.send(message);
  }
}



