package com.example.LIANJOY.service;

import com.example.LIANJOY.model.Usuario;
import com.example.LIANJOY.repository.UsuarioReposity; // Verifica si tu interfaz se llama así o Repository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioReposity usuarioReposity; // Revisa que el nombre de la CLASE sea este

   @Override
public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    // Usamos .orElseThrow para que si no existe, lance la excepción de una vez
    Usuario usuario = usuarioReposity.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

    return User.withUsername(usuario.getEmail())
               .password(usuario.getPassword()) 
               .roles("USER")
               .build();
}
}