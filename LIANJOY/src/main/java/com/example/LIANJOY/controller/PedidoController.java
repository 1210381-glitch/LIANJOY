package com.example.LIANJOY.controller;

import com.example.LIANJOY.model.Pedido;
import com.example.LIANJOY.model.Producto;
import com.example.LIANJOY.model.Usuario;
import com.example.LIANJOY.repository.PedidoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    // 1. Mostrar la página de checkout
    @GetMapping("/checkout")
    public String irACheckout(HttpSession session, Model model) {
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        
        if (carrito == null || carrito.isEmpty()) {
            return "redirect:/carrito";
        }

        double total = carrito.stream()
                             .mapToDouble(p -> p.getPrecio() != null ? p.getPrecio() : 0.0)
                             .sum();
                             
        model.addAttribute("total", total);
        return "checkout";
    }

    // 2. Procesar datos de envío y pasar a pago_paypal
    @PostMapping("/pedido/confirmar")
    public String confirmarPedido(
            @RequestParam String pais,
            @RequestParam String ciudad,
            @RequestParam String codigoPostal,
            @RequestParam String direccion,
            HttpSession session, Model model) {
    
        try {
            // Guardamos la dirección completa formateada para el admin
            String direccionCompleta = direccion + ", " + ciudad + ", " + pais + " (CP: " + codigoPostal + ")";
            session.setAttribute("envioDireccion", direccionCompleta);

            List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
            if (carrito == null || carrito.isEmpty()) return "redirect:/carrito";

            double total = carrito.stream().mapToDouble(p -> p.getPrecio() != null ? p.getPrecio() : 0.0).sum();
            model.addAttribute("total", total);

            return "pago_paypal"; 
        } catch (Exception e) {
            System.out.println("Error en pedido: " + e.getMessage());
            return "redirect:/checkout?error";
        }
    }

    // 3. Confirmación de éxito, GUARDADO EN BD y limpieza del carrito
    @GetMapping("/pedido/exito")
    public String mostrarExito(HttpSession session, Model model) {
        List<Producto> productosComprados = (List<Producto>) session.getAttribute("carrito");
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        String direccion = (String) session.getAttribute("envioDireccion");
        
        // Seguridad: Verificar que haya sesión y productos
        if (productosComprados == null || usuario == null) {
            return "redirect:/"; 
        }

        try {
            // --- ¡GUARDAMOS EL PEDIDO EN LA BASE DE DATOS! ---
            Pedido nuevoPedido = new Pedido();
            nuevoPedido.setUsuario(usuario);
            nuevoPedido.setClienteEmail(usuario.getEmail());
            nuevoPedido.setDireccion(direccion);
            nuevoPedido.setEstadoPedido("PAGADO"); // Aparecerá así en el panel de Milton
            
            double total = productosComprados.stream()
                                             .mapToDouble(p -> p.getPrecio() != null ? p.getPrecio() : 0.0)
                                             .sum();
            nuevoPedido.setTotal(total);

            // Guardamos físicamente en la tabla 'pedidos'
            pedidoRepository.save(nuevoPedido);
            System.out.println("--- PEDIDO GUARDADO PARA EL ADMIN: " + nuevoPedido.getId() + " ---");

            // Pasar datos a la vista de éxito
            model.addAttribute("productos", productosComprados);
            model.addAttribute("total", total);
            model.addAttribute("direccion", direccion);

            // ¡PASO FINAL! Limpiamos el carrito para que el contador vuelva a 0
            session.removeAttribute("carrito");
            
            return "pago_exito";

        } catch (Exception e) {
            System.err.println("Error al guardar pedido: " + e.getMessage());
            return "redirect:/";
        }
    }
}