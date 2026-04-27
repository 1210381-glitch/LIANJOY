package com.example.LIANJOY.controller;

import com.example.LIANJOY.model.Producto;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PedidoController {

    // 1. Mostrar la página de checkout (Captura de dirección)
    @GetMapping("/checkout")
    public String irACheckout(HttpSession session, Model model) {
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        
        // Si el carrito está vacío, lo mandamos de vuelta para evitar errores
        if (carrito == null || carrito.isEmpty()) {
            return "redirect:/carrito";
        }

        // Calculamos el total para mostrarlo en el resumen del checkout
        double total = carrito.stream()
                             .mapToDouble(p -> p.getPrecio() != null ? p.getPrecio() : 0.0)
                             .sum();
                             
        model.addAttribute("total", total);
        return "checkout";
    }

    // 2. Procesar datos de envío y pasar a la pasarela de PayPal
    @PostMapping("/pedido/confirmar")
    public String confirmarPedido(
            @RequestParam("pais") String pais,
            @RequestParam("ciudad") String ciudad,
            @RequestParam("codigoPostal") String cp,
            @RequestParam("direccion") String direccion,
            HttpSession session, Model model) {
        
        // Guardamos los datos en la sesión para usarlos en la confirmación final
        session.setAttribute("envioPais", pais);
        session.setAttribute("envioCiudad", ciudad);
        session.setAttribute("envioDireccion", direccion);

        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        double total = (carrito != null) ? 
                       carrito.stream().mapToDouble(p -> p.getPrecio() != null ? p.getPrecio() : 0.0).sum() : 0.0;
        
        model.addAttribute("total", total);
        
        // Redirige a la vista donde está el botón amarillo de PayPal
        return "pago_paypal"; 
    }

    // 3. Confirmación de éxito y LIMPIEZA del carrito
    @GetMapping("/pedido/exito")
    public String mostrarExito(HttpSession session, Model model) {
        // Obtenemos los datos antes de borrar la sesión para la vista de agradecimiento
        List<Producto> productosComprados = (List<Producto>) session.getAttribute("carrito");
        String direccion = (String) session.getAttribute("envioDireccion");
        
        // Seguridad: Si no hay productos en sesión, no hay nada que confirmar
        if (productosComprados == null) {
            return "redirect:/"; 
        }

        // Calculamos el total final cobrado
        double total = productosComprados.stream()
                                         .mapToDouble(p -> p.getPrecio() != null ? p.getPrecio() : 0.0)
                                         .sum();

        model.addAttribute("productos", productosComprados);
        model.addAttribute("total", total);
        model.addAttribute("direccion", direccion);

        // ¡PASO FINAL! Limpiamos el carrito para que el contador vuelva a 0
        session.removeAttribute("carrito");
        
        return "pago_exito";
    }
}