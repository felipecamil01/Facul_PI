package com.Advocacia.ConfigTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.Advocacia.Config.SecurityConfig;

class SecurityConfigTest {

    @Test
    void testPasswordEncoderBean() {
        ApplicationContext context = new AnnotationConfigApplicationContext(SecurityConfig.class);

        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);

        assertNotNull(passwordEncoder);
    }
}