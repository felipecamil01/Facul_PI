package com.Advocacia.Service;
/*

import com.Advocacia.Auth.Usuario;
import com.Advocacia.Config.JwtServiceGenerator;
import com.Advocacia.Entity.User;
import com.Advocacia.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtServiceGenerator jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;

    public Usuario salvar(Usuario usuario){
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

    return userRepository.save(usuario);
    }

    public boolean VerificarCredencias (String senhaInserida, User user){
        return passwordEncoder.matches(senhaInserida, user.getPassword());



    }
    public String logar(Usuario user) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );
        Usuario username = userRepository.findByUsername(user.getUsername()).get();
        String jwtToken = jwtService.generateToken(username);

        return jwtToken;
}
    }*/
