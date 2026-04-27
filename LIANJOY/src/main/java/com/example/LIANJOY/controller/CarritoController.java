package com.example.LIANJOY.controller;

import com.example.LIANJOY.model.Producto;
import com.example.LIANJOY.repository.ProductoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CarritoController {

    @Autowired
    private ProductoRepository productoRepository;

    // 1. Ver la página del carrito y calcular el total
    @GetMapping("/carrito")
    public String mostrarCarrito(HttpSession session, Model model) {
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        // Enviamos la lista de productos al HTML
        model.addAttribute("productosCarrito", carrito);

        // CÁLCULO DEL TOTAL (MXN): Sumamos el precio de cada producto
        double sumaTotal = carrito.stream()
                                  .mapToDouble(p -> p.getPrecio() != null ? p.getPrecio() : 0.0)
                                  .sum();
        
        model.addAttribute("total", sumaTotal);

        return "carrito";
    }

    // 2. Lógica para agregar productos (Desde la tienda principal)
    @PostMapping("/carrito/agregar")
    public String agregarAlCarrito(@RequestParam("productoId") Integer productoId, HttpSession session) {
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        
        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        Optional<Producto> productoOptional = productoRepository.findById(productoId);
        
        if (productoOptional.isPresent()) {
            carrito.add(productoOptional.get());
        }

        session.setAttribute("carrito", carrito);
        return "redirect:/"; 
    }

    // 3. NUEVO: Lógica para eliminar productos (Soluciona el error 404)
    @PostMapping("/carrito/eliminar")
    public String eliminarProducto(@RequestParam("id") Integer id, HttpSession session) {
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        
        if (carrito != null) {
            // Buscamos y eliminamos el producto por su ID
            // Nota: Usamos removeIf para quitarlo de la lista en memoria
            carrito.removeIf(p -> p.getId().equals(id));
            
            // Actualizamos la sesión con la lista nueva
            session.setAttribute("carrito", carrito);
        }
        
        // Redirigimos de vuelta al carrito para ver el cambio y el nuevo total
        return "redirect:/carrito";
    }
}