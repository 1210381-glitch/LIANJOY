package com.example.LIANJOY.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Data;

@Entity
@Table(name = "productos")
@Data
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;

    @Column(name = "imagen_principal")
    private String imagenPrincipal;

    @ManyToOne
    @JoinColumn(name = "categorias_id")
    private Categoria categoria;

    // Getters y Setters
}