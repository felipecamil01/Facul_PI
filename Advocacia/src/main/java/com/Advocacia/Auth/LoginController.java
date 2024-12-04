package com.Advocacia.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {

	@Autowired
	private LoginService loginService;

  @Autowired
  private UsuarioService usuarioService;

  @PostMapping
	public ResponseEntity<String> logar(@RequestBody Login login) {
		try {
			return ResponseEntity.ok(loginService.logar(login));
		}catch(AuthenticationException ex) {
			System.out.println(ex.getMessage());
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		}catch (Exception e) {
			System.out.println(e.getMessage());
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


  @PutMapping("/alterar-senha")
  public ResponseEntity<String> alterarSenha(@RequestParam String username, @RequestParam String senhaAtual, @RequestParam String novaSenha) {
    try {
      usuarioService.alterarSenha(username, senhaAtual, novaSenha);
      return ResponseEntity.ok("Senha alterada com sucesso!");
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }
}

