package com.example.LIANJOY.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import com.example.LIANJOY.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class HomeController {

    @Autowired
    private ProductoRepository productoRepo;

    @GetMapping("/")
    public String verHome(Model model) {
        // Buscamos todos los productos para que aparezcan en el index
        model.addAttribute("productos", productoRepo.findAll());
        return "index"; 
    }
}