package com.example.LIANJOY.controller;

import com.example.LIANJOY.model.Usuario;
import com.example.LIANJOY.repository.UsuarioReposity;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UsuarioReposity usuarioRepository;

    @GetMapping("/acceso")
    public String mostrarAcceso(Model model) {
        if (!model.containsAttribute("usuario")) {
            model.addAttribute("usuario", new Usuario());
        }
        return "acceso"; 
    }

    @PostMapping("/api/auth/procesar-acceso")
    public String procesarAcceso(@ModelAttribute Usuario usuarioData, HttpSession session, RedirectAttributes redirectAttrs) {
        try {
            // Validación de campos vacíos
            if (usuarioData.getEmail() == null || usuarioData.getPassword() == null || 
                usuarioData.getEmail().isBlank() || usuarioData.getPassword().isBlank()) {
                redirectAttrs.addFlashAttribute("error", "Por favor, completa todos los campos.");
                return "redirect:/acceso";
            }

            String inputIdentificador = usuarioData.getEmail().trim();
            String pass = usuarioData.getPassword().trim();

            // --- 1. PRIORIDAD ABSOLUTA: VALIDACIÓN DE ADMINISTRADOR (MILTON) ---
            // Verificamos si ingresó su nombre de usuario O su correo electrónico oficial
            boolean esMilton = inputIdentificador.equalsIgnoreCase("miltonlazaro") || 
                               inputIdentificador.equalsIgnoreCase("miltonlazaro@gmail.com");

            if (esMilton && pass.equals("LIANJOY")) {
                // Buscamos a Milton por su correo oficial para mantener consistencia en la DB
                Optional<Usuario> miltonDB = usuarioRepository.findByEmail("miltonlazaro@gmail.com");
                
                if (miltonDB.isPresent()) {
                    session.setAttribute("usuarioLogueado", miltonDB.get());
                } else {
                    // Si Milton no existe físicamente en la DB, lo creamos con su correo oficial
                    Usuario miltonNuevo = new Usuario();
                    miltonNuevo.setEmail("miltonlazaro@gmail.com");
                    miltonNuevo.setPassword("LIANJOY");
                    usuarioRepository.save(miltonNuevo);
                    session.setAttribute("usuarioLogueado", miltonNuevo);
                }
                
                session.setAttribute("esAdmin", true); 
                System.out.println("--- ACCESO COMO ADMINISTRADOR CONFIRMADO: " + inputIdentificador + " ---");
                return "redirect:/admin/panel"; 
            }

            // --- 2. LÓGICA PARA USUARIOS NORMALES ---
            Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(inputIdentificador);

            if (usuarioExistente.isPresent()) {
                Usuario userDB = usuarioExistente.get();
                
                if (userDB.getPassword().equals(pass)) {
                    session.setAttribute("usuarioLogueado", userDB);
                    System.out.println("Login exitoso para cliente: " + inputIdentificador);
                    return "redirect:/"; 
                } else {
                    redirectAttrs.addFlashAttribute("error", "Contraseña incorrecta.");
                    return "redirect:/acceso";
                }
            } else {
                // --- 3. LÓGICA DE REGISTRO AUTOMÁTICO PARA CLIENTES NUEVOS ---
                System.out.println("--- EMAIL NO ENCONTRADO. REGISTRANDO CLIENTE NUEVO: " + inputIdentificador + " ---");
                usuarioData.setEmail(inputIdentificador);
                usuarioData.setPassword(pass);
                Usuario nuevo = usuarioRepository.save(usuarioData);
                session.setAttribute("usuarioLogueado", nuevo);
                return "redirect:/";
            }

        } catch (Exception e) {
            System.err.println("ERROR CRÍTICO EN AUTH: " + e.getMessage());
            redirectAttrs.addFlashAttribute("error", "Error técnico: " + e.getMessage());
            return "redirect:/acceso";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); 
        return "redirect:/acceso";
    }
}