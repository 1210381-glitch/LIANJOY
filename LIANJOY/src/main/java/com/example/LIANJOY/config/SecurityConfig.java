package com.example.LIANJOY.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desactiva CSRF para poder usar Postman/Login
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/api/auth/**", "/css/**", "/js/**", "/images/**").permitAll() // Rutas libres
                .anyRequest().authenticated() // Lo demás pide login
            )
            .formLogin(login -> login.permitAll()); // Permite el formulario por defecto
        
        return http.build();
    }
}