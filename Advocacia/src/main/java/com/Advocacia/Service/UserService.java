package com.Advocacia.Service;

import com.Advocacia.Entity.User;
import com.Advocacia.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public void salvarUsuario(User user){
        String senhaCrip=passwordEncoder.encode(user.getPassword());
        user.setPassword(senhaCrip);


    userRepository.save(user);
    }

    public boolean VerificarCredencias (String senhaInserida, User user){
        return passwordEncoder.matches(senhaInserida, user.getPassword());


    }
}
