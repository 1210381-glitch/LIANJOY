package com.example.LIANJOY.controller;

import com.example.LIANJOY.service.PedidoService;
import com.example.LIANJOY.model.*;
import com.example.LIANJOY.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Importación necesaria para el objeto Model
import org.springframework.web.bind.annotation.*;

import java.util.List; // Importación necesaria para las listas

@Controller
@RequestMapping("/carrito")
public class PedidoController {

    @Autowired private PedidoService pedidoService;
    @Autowired private ProductoRepository productoRepo;
    @Autowired private UsuarioReposity usuarioRepo;
    @Autowired private DetallePedidoRepository detallePedidoRepo; // Añadido para el resumen

    // Método para agregar un producto al pedido (Acción POST)
    @PostMapping("/agregar/{id}")
    public String agregarAlCarrito(@PathVariable Integer id) {
        // Por ahora usamos el usuario ID 1 como prueba (Simulando login)
        Usuario user = usuarioRepo.findById(1).orElse(null);
        Producto prod = productoRepo.findById(id).orElse(null);

        if (user != null && prod != null) {
            pedidoService.crearPedido(user, prod, 1);
        }
        return "redirect:/"; // Regresa a la página principal
    }

    // Método para ver la vista del carrito (Acción GET)
    @GetMapping("/resumen")
    public String verResumen(Model model) {
        // Simulamos que el usuario logueado es el ID: 1
        // Buscamos todos los detalles registrados en la tabla dbo.detalle_pedidos
        List<DetallePedido> detalles = detallePedidoRepo.findAll(); 
        
        // Calculamos el total de la compra sumando cada detalle
        double totalGeneral = detalles.stream()
                .mapToDouble(d -> d.getPrecioUnitario() * d.getCantidad())
                .sum();

        // Enviamos los datos a la vista carrito.html
        model.addAttribute("detalles", detalles);
        model.addAttribute("totalGeneral", totalGeneral);
        
        return "carrito"; // Redirige a carrito.html
    }

    // Método para eliminar un item del carrito
@PostMapping("/eliminar/{id}")
public String eliminarDelCarrito(@PathVariable Integer id) {
    // Borramos el registro directamente de la tabla detalle_pedidos
    detallePedidoRepo.deleteById(id);
    
    // Redirigimos de nuevo al resumen para que vea el cambio
    return "redirect:/carrito/resumen";
}
}