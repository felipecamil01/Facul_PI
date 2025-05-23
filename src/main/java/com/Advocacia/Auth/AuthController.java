package com.Advocacia.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<String> logar(@RequestBody Login login) {
        try {
            return ResponseEntity.ok(loginService.logar(login));
        } catch (AuthenticationException ex) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/criar")
    public ResponseEntity<Usuario> criarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario novoUsuario = usuarioService.criarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
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
    public ResponseEntity<Usuario> atualizarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario usuarioAtualizado = usuarioService.atualizarUsuario(usuario);
            return ResponseEntity.ok(usuarioAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

  @PostMapping("/recuperar-senha/{email}")
  public ResponseEntity<Void> solicitarRecuperacaoSenha(@PathVariable String email) {
    loginService.gerarTokenRecuperacao(email);
    return ResponseEntity.ok().build();
  }




  @PostMapping("/validar-token")
    public ResponseEntity<?> validarToken(
            @RequestParam String email,
            @RequestParam String token) {
        try {
            boolean tokenValido = loginService.validarTokenRecuperacao(email, token);
            return ResponseEntity.ok(tokenValido);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Token inválido");
        }
    }

  @PostMapping("/savePassword")
  public ResponseEntity<?> savePassword(
    @RequestParam("email") String email,
    @RequestParam("token") String token,
    @RequestParam("novaSenha") String newPassword) {

    boolean tokenValido = loginService.validarTokenRecuperacao(email, token);

    if (!tokenValido) {
      throw new RuntimeException("Token inválido ou expirado");
    }

    Usuario user = usuarioRepository.findByEmail(email)
      .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
    usuarioRepository.save(user);

    Map<String, String> response = new HashMap<>();
    response.put("message", "Senha alterada com sucesso");
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
  }

}
