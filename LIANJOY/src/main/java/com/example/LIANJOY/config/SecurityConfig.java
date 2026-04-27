package com.example.LIANJOY.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) 
            .authorizeHttpRequests(auth -> auth
                // 1. Rutas públicas: Tienda, Login, Carrito y Recursos estáticos
                .requestMatchers(
                    "/", 
                    "/acceso", 
                    "/carrito/**", 
                    "/css/**", 
                    "/js/**", 
                    "/imagenes/**", 
                    "/favicon.ico"
                ).permitAll()
                
                // 2. Cualquier otra ruta requiere estar logueado
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/acceso") // Tu página personalizada
                .loginProcessingUrl("/login") 
                .defaultSuccessUrl("/", true) // Obliga a ir a la tienda tras el login
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/") 
                .permitAll()
            );

        return http.build();
    }

    // Bean para encriptar/leer contraseñas de la base de datos
    @Bean
public PasswordEncoder passwordEncoder() {
    // Esto permite que Spring lea las contraseñas en texto plano (NO RECOMENDADO PARA PRODUCCIÓN)
    return org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
}
}