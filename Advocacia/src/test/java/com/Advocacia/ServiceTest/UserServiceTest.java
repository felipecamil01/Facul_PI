package com.Advocacia.ServiceTest;

import com.Advocacia.Auth.Usuario;
import com.Advocacia.Entity.User;
import com.Advocacia.Repository.UserRepository;
import com.Advocacia.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class UserServiceTest {
//
//    @InjectMocks
//    private UserService userService;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    private Usuario user;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        user = new User();
//        user.setId(1L);
//        user.setUsername("testUser");
//        user.setPassword("testPassword");
//    }
//
//    @Test
//    void testSalvarUsuario() {
//        String senhaCriptografada = "senhaCriptografada";
//
//        Mockito.when(passwordEncoder.encode(anyString())).thenReturn(senhaCriptografada);
//        Mockito.when(userRepository.save(any(Usuario.class))).thenReturn(user);
//
//        userService.salvar(user);
//
//        assertEquals(senhaCriptografada, user.getPassword());
//        Mockito.verify(userRepository, Mockito.times(1)).save(user);
//    }
//
//    @Test
//    void testVerificarCredencias() {
//        String senhaInserida = "testPassword";
//
//        Mockito.when(passwordEncoder.matches(senhaInserida, user.getPassword())).thenReturn(true);
//
//        boolean credenciaisValidas = userService.VerificarCredencias(senhaInserida,user);
//
//        assertTrue(credenciaisValidas);
//        Mockito.verify(passwordEncoder, Mockito.times(1)).matches(senhaInserida, user.getPassword());
//    }
//
//    @Test
//    void testVerificarCredenciasIncorretas() {
//        String senhaInserida = "senhaErrada";
//
//        Mockito.when(passwordEncoder.matches(senhaInserida, user.getPassword())).thenReturn(false);
//
//        boolean credenciaisValidas = userService.VerificarCredencias(senhaInserida, user);
//
//        assertFalse(credenciaisValidas);
//        Mockito.verify(passwordEncoder, Mockito.times(1)).matches(senhaInserida, user.getPassword());
//    }
}
