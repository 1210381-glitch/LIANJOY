package com.example.LIANJOY.controller;

import com.example.LIANJOY.model.Usuario;
import com.example.LIANJOY.repository.UsuarioReposity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Permite que tu frontend se conecte sin bloqueos
public class AuthController {

    @Autowired
    private UsuarioReposity usuarioRepository;

    @PostMapping("/login")
    public String login(@RequestBody Usuario loginData) {
        // Buscamos al usuario por el email que llega en el JSON
        Optional<Usuario> user = usuarioRepository.findByEmail(loginData.getEmail());

        if (user.isPresent() && user.get().getPassword().equals(loginData.getPassword())) {
            return "Login exitoso. ¡Bienvenido " + user.get().getNombre() + "!";
        } else {
            return "Error: Credenciales incorrectas.";
        }
    }
}