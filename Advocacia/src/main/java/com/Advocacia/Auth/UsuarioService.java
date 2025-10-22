package com.Advocacia.Auth;

import com.Advocacia.DTO.RegistroComTokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RegistrationTokenService registrationTokenService;

    public Usuario criarUsuario(RegistroComTokenDTO dto) {
        if (dto.getUsername() == null || dto.getUsername().isBlank()) {
            throw new IllegalArgumentException("Informe um nome de usuário.");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new IllegalArgumentException("Informe uma senha.");
        }
        if (usuarioRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Já existe um usuário com este nome.");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));

        if (usuarioRepository.count() == 0) {
            usuario.setRole(UserRole.ADMIN);
            return usuarioRepository.save(usuario);
        }

        if (dto.getToken() == null || dto.getToken().isBlank()) {
            throw new IllegalArgumentException("É necessário informar um token de registro válido.");
        }

        RegistrationToken registrationToken = registrationTokenService.validarTokenDisponivel(dto.getToken(), dto.getEmail());
        usuario.setRole(registrationToken.getRole() != null ? registrationToken.getRole() : UserRole.SECRETARIA);

        Usuario salvo = usuarioRepository.save(usuario);
        registrationTokenService.marcarComoUsado(registrationToken, salvo.getId());
        return salvo;
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
        usuarioExistente.setRole(usuario.getRole());

        return usuarioRepository.save(usuarioExistente);
    }

    public void alterarSenha(String username, String senhaAtual, String novaSenha) {
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(senhaAtual, usuario.getPassword())) {
            throw new RuntimeException("Senha atual incorreta");
        }

        usuario.setPassword(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);
    }
}
