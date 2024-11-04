package com.Advocacia.Controller;
/*
import com.Advocacia.Auth.Usuario;
import com.Advocacia.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import com.Advocacia.Entity.User;
import com.Advocacia.Service.UserService;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")
public class UserController {





    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired

    private PasswordEncoder encoder;

    @GetMapping("/listarTodos")
    public ResponseEntity<List<Usuario>>listarTodos(){

        return ResponseEntity.ok(userRepository.findAll());
    }


	@PostMapping("/salvar")

    public ResponseEntity<Usuario> salvar(@RequestBody Usuario  user) {
  user.setPassword(encoder.encode(user.getPassword()));

        return ResponseEntity.ok(userRepository.save(user));
    }

    @GetMapping("/validaSenha")
    public ResponseEntity<Boolean>validaSenha(@RequestParam String user,@RequestParam String password){
        Optional<Usuario>optUser = userRepository.findByUsername(user);
        if (optUser.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);

        }
    Usuario user2 = optUser.get();
        boolean valid = encoder.matches(password, user2.getPassword());

        HttpStatus status = (valid)? HttpStatus.OK: HttpStatus.UNAUTHORIZED;

        return ResponseEntity.status(status).body(valid);
    }

    @PostMapping("/logar")
    public ResponseEntity<String> logar(@RequestBody Usuario user) {
        try {
            return ResponseEntity.ok(userService.logar(user));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
 */
