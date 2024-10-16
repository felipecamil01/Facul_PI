package com.Advocacia.Controller;

import com.Advocacia.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import com.Advocacia.Entity.User;
import com.Advocacia.Service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
     private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;

    @GetMapping("/listarTodos")
    public ResponseEntity<List<User>>listarTodos(){
        return ResponseEntity.ok(userRepository.findAll());
    }


	@PostMapping("/salvar")

    public ResponseEntity<User> salvar(@RequestBody User user) {
  user.setPassword(encoder.encode(user.getPassword()));

        return ResponseEntity.ok(userRepository.save(user));
    }
    @GetMapping("/validaSenha")
    public ResponseEntity<Boolean>validaSenha(@RequestParam String user,@RequestParam String password){
        Optional<User>optUser = userRepository.findByUser(user);
        if (optUser.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);

        }
    User user2 = optUser.get();
        boolean valid = encoder.matches(password, user2.getPassword());

        HttpStatus status = (valid)? HttpStatus.OK: HttpStatus.UNAUTHORIZED;

        return ResponseEntity.status(status).body(valid);
    }
}