package com.example.LIANJOY.service;

import com.example.LIANJOY.model.Usuario;
import com.example.LIANJOY.repository.UsuarioReposity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioReposity usuarioRepository;

    public Usuario login(String email, String password) {
        Optional<Usuario> user = usuarioRepository.findByEmail(email);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user.get();
        }
        return null; // En un proyecto real, lanzaríamos una excepción
    }
}