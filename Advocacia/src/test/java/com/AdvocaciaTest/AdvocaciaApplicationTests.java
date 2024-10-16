package com.AdvocaciaTest;

import com.Advocacia.AdvocaciaApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
class AdvocaciaApplicationTests {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    void contextLoads() {
//    }

//    @TestConfiguration
//    static class TestConfig {
//        @Bean
//        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {http.csrf().disable().authorizeHttpRequests().anyRequest().permitAll();
//            return http.build();
//        }
//    }
}