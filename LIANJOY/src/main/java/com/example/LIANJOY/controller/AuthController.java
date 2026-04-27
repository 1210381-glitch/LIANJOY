package com.example.LIANJOY.controller;

import com.example.LIANJOY.model.Usuario;
import com.example.LIANJOY.repository.UsuarioReposity;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UsuarioReposity usuarioRepository;

    /**
     * 1. Muestra la pantalla de acceso unificada (Login/Registro).
     * Carga el formulario con el diseño estilo Amazon que configuramos.
     */
    @GetMapping("/acceso")
    public String mostrarAcceso(Model model) {
        // Enviamos un objeto Usuario vacío para vincularlo con th:object en el HTML
        model.addAttribute("usuario", new Usuario());
        return "acceso"; 
    }

    /**
     * 2. Procesa la lógica inteligente de acceso.
     * Si el email existe, intenta iniciar sesión.
     * Si el email es nuevo, registra al usuario con nombre, email, password y teléfono.
     */
    @PostMapping("/api/auth/procesar-acceso")
public String procesarAcceso(@ModelAttribute Usuario usuarioData, HttpSession session, RedirectAttributes redirectAttrs) {
    Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuarioData.getEmail());

    if (usuarioExistente.isPresent()) {
        Usuario userDB = usuarioExistente.get();
        if (userDB.getPassword().equals(usuarioData.getPassword())) {
            // IMPORTANTE: Guardar al usuario en la sesión para que el sistema lo reconozca
            session.setAttribute("usuarioLogueado", userDB);
            return "redirect:/"; 
        } else {
            redirectAttrs.addFlashAttribute("error", "Contraseña incorrecta");
            return "redirect:/acceso";
        }
    } else {
        // Registro nuevo
        Usuario nuevo = usuarioRepository.save(usuarioData);
        session.setAttribute("usuarioLogueado", nuevo); // También logueamos al nuevo
        return "redirect:/";
    }
}

    /**
     * 3. Ruta de Logout (Opcional).
     */
    @GetMapping("/logout")
    public String logout() {
        // Aquí puedes añadir lógica para limpiar cookies o sesiones en el futuro
        return "redirect:/acceso";
    }
}