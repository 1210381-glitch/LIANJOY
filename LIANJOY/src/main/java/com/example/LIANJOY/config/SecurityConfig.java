package com.example.LIANJOY.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Desactivar CSRF para permitir peticiones POST desde nuestro formulario
            .csrf(csrf -> csrf.disable()) 
            
            .authorizeHttpRequests(auth -> auth
                // 2. Rutas totalmente públicas (Sin necesidad de login)
               .requestMatchers(
    "/", 
    "/acceso", 
    "/api/auth/**", 
    "/carrito/**", 
    "/checkout",
    "/pedido/**",
    "/pago_paypal",
    "/pago_exitoso",
    "/admin/**",        // <--- AGREGA ESTA LÍNEA para que Milton pueda entrar al panel
    "/css/**", 
    "/js/**", 
    "/imagenes/**", 
    "/favicon.ico"
).permitAll()
                // 3. Cualquier otra ruta (como un panel de admin futuro) requiere login
                .anyRequest().authenticated()
            )
            
            // 4. Configuración de Logout
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/") 
                .permitAll()
            );

        return http.build();
    }
@Bean
    public org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/admin/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
    }
}