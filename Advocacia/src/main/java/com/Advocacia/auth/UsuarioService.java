package com.Advocacia.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

  @Autowired
  private LoginRepository loginRepository;

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public Usuario criarUsuario(Usuario usuario) {
    // Validações
    if (usuario.getUsername() == null || usuario.getUsername().isEmpty()) {
      throw new RuntimeException("Username é obrigatório");
    }

    if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
      throw new RuntimeException("Senha é obrigatória");
    }

    // Verificar se usuário já existe
    if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
      throw new RuntimeException("Usuário já cadastrado");
    }

    // Criptografar senha
    usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

    // Verificar se é o primeiro usuário
    if (usuarioRepository.count() == 0) { // Se o banco não tiver usuários
      usuario.setRole("ROLE_ADMIN");
    } else {
      usuario.setRole("ROLE_USER");
    }

    return usuarioRepository.save(usuario);
  }

  public Optional<Usuario> buscarPorUsername(String username) {
    return usuarioRepository.findByUsername(username);
  }

  public Optional<Usuario> buscarPorEmail(String email) {
    return usuarioRepository.findByEmail(email);
  }

  public Usuario atualizarUsuario(Usuario usuario) {
    Usuario usuarioExistente = usuarioRepository.findById(usuario.getId())
      .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    usuarioExistente.setUsername(usuario.getUsername());
    usuarioExistente.setEmail(usuario.getEmail());

    return usuarioRepository.save(usuarioExistente);
  }



  public void alterarSenha(String username, String senhaAtual, String novaSenha) {
    Usuario usuario = usuarioRepository.findByUsername(username)
      .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    // Verificar senha atual
    if (!passwordEncoder.matches(senhaAtual, usuario.getPassword())) {
      throw new RuntimeException("Senha atual incorreta");
    }

    // Criptografar nova senha
    usuario.setPassword(passwordEncoder.encode(novaSenha));
    usuarioRepository.save(usuario);
  }
}
