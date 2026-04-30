package com.example.LIANJOY.controller;

import com.example.LIANJOY.model.Pedido;
import com.example.LIANJOY.model.Producto;
import com.example.LIANJOY.repository.PedidoRepository;
import com.example.LIANJOY.repository.ProductoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // 1. Ver el Panel (Inventario + Pedidos)
    @GetMapping("/admin/panel")
    public String verPanel(HttpSession session, Model model) {
        // SEGURIDAD: Solo Milton entra
        if (session.getAttribute("esAdmin") == null) {
            return "redirect:/acceso";
        }

        // Cargar Pedidos (Lista de ventas)
        List<Pedido> listaPedidos = pedidoRepository.findAll();
        model.addAttribute("pedidos", listaPedidos);

        // Cargar Productos (Inventario actual)
        model.addAttribute("productos", productoRepository.findAll());

        return "admin_panel"; 
    }

    // 2. Procesar la creación de un nuevo producto (Lógica Híbrida)
    @PostMapping("/admin/productos/guardar")
    public String guardarProducto(
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") Double precio,
            @RequestParam("stock") Integer stock,
            @RequestParam("imagenUrl") String imagenUrl,
            @RequestParam("archivoImagen") MultipartFile archivo, // Captura el archivo físico
            HttpSession session) {

        // Validación de seguridad
        if (session.getAttribute("esAdmin") == null) return "redirect:/acceso";

        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setStock(stock);

        // --- LÓGICA HÍBRIDA DE IMAGEN ---
        if (archivo != null && !archivo.isEmpty()) {
            try {
                // 1. Definir la ruta relativa a la carpeta del proyecto
                String nombreImagen = archivo.getOriginalFilename();
                // Asegúrate de que esta carpeta exista: src/main/resources/static/imagenes/
                Path rutaDirecta = Paths.get("src/main/resources/static/imagenes/" + nombreImagen);
                
                // 2. Guardar el archivo físicamente en el servidor
                Files.write(rutaDirecta, archivo.getBytes());
                
                // 3. Guardar la ruta que usará el navegador en la BD
                producto.setImagenUrl("/imagenes/" + nombreImagen);
                System.out.println("Archivo guardado localmente: " + nombreImagen);
                
            } catch (IOException e) {
                System.err.println("Error al guardar el archivo: " + e.getMessage());
                e.printStackTrace();
            }
        } else if (imagenUrl != null && !imagenUrl.isBlank()) {
            // Si no hay archivo físico, usamos la URL externa proporcionada
            producto.setImagenUrl(imagenUrl);
            System.out.println("Usando URL externa para la imagen.");
        }

        productoRepository.save(producto);
        System.out.println("--- PRODUCTO GUARDADO EXITOSAMENTE: " + nombre + " ---");

        return "redirect:/admin/panel";
    }
}