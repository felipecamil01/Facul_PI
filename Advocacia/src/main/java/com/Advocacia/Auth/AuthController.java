package com.Advocacia.Auth;

import com.Advocacia.DTO.RegistroComTokenDTO;
import com.Advocacia.DTO.TokenSolicitacaoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/login")
@CrossOrigin("*")
public class AuthController {

  @Autowired
  private LoginService loginService;

  @Autowired
  private UsuarioService usuarioService;

  @Autowired
  private RegistrationTokenService registrationTokenService;

  @PostMapping
  public ResponseEntity<String> logar(@RequestBody Login login) {
    String token = loginService.logar(login);
    return ResponseEntity.ok(token);
  }

  @PostMapping("/criar")
  public ResponseEntity<Usuario> criarUsuario(@RequestBody RegistroComTokenDTO dto) {
    Usuario novoUsuario = usuarioService.criarUsuario(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
  }

  @PostMapping("/token")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Map<String, String>> gerarTokenDeRegistro(@RequestBody TokenSolicitacaoDTO request) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Long createdBy = null;
    if (authentication != null && authentication.getPrincipal() instanceof Usuario usuarioAutenticado) {
      createdBy = usuarioAutenticado.getId();
    }

    RegistrationToken token = registrationTokenService.generateToken(
      request.getEmail(),
      request.getRole(),
      request.getValidadeHoras(),
      createdBy
    );

    Map<String, String> resposta = new HashMap<>();
    resposta.put("token", token.getToken());
    resposta.put("expiraEm", token.getExpiresAt().toString());
    resposta.put("role", token.getRole().name());
    return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
  }

  @GetMapping("/token/{token}")
  public ResponseEntity<Map<String, Object>> validarToken(@PathVariable String token, @RequestParam(required = false) String email) {
    RegistrationToken registrationToken = registrationTokenService.validarTokenDisponivel(token, email);
    Map<String, Object> resposta = new HashMap<>();
    resposta.put("token", registrationToken.getToken());
    resposta.put("email", registrationToken.getEmail());
    resposta.put("role", registrationToken.getRole());
    resposta.put("expiraEm", registrationToken.getExpiresAt());
    return ResponseEntity.ok(resposta);
  }

  @GetMapping("/username/{username}")
  public ResponseEntity<Usuario> buscarPorUsername(@PathVariable String username) {
    return usuarioService.buscarPorUsername(username)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
  }

  @GetMapping("/email/{email}")
  public ResponseEntity<Usuario> buscarPorEmail(@PathVariable String email) {
    return usuarioService.buscarPorEmail(email)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
  }

  @PutMapping("/atualiza-user")
  @PreAuthorize("hasAnyRole('ADMIN')")
  public ResponseEntity<Usuario> atualizarUsuario(@RequestBody Usuario usuario) {
      Usuario usuarioAtualizado = usuarioService.atualizarUsuario(usuario);
      return ResponseEntity.ok(usuarioAtualizado);
  }

  @PostMapping("/recuperar-senha/{email}")
  public ResponseEntity<Void> solicitarRecuperacaoSenha(@PathVariable String email) {
    loginService.gerarTokenRecuperacao(email);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/validar-token")
  public ResponseEntity<?> validarTokenRecuperacao(@RequestParam String email, @RequestParam String token) {
      boolean tokenValido = loginService.validarTokenRecuperacao(email, token);
      return ResponseEntity.ok(tokenValido);
  }

  @PostMapping("/savePassword")
  public ResponseEntity<?> savePassword(@RequestParam("email") String email,
                                        @RequestParam("token") String token,
                                        @RequestParam("novaSenha") String newPassword) {
    try {
      loginService.redefinirSenha(email, token, newPassword);
      Map<String, String> response = new HashMap<>();
      response.put("message", "Senha alterada com sucesso");

      return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao alterar a senha");
    }
  }
}
