package com.example.LIANJOY.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "productos", schema = "dbo")
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    private Double precio; 

    private Integer stock;

    // VINCULACIÓN: Mapeo exacto a la columna de la DB
    @Column(name = "imagen_url") 
    private String imagenUrl;

    @Column(length = 1000) 
    private String descripcion; 

    // --- MÉTODOS GETTERS Y SETTERS ---

    public Integer getId() { 
        return id; 
    }
    public void setId(Integer id) { 
        this.id = id; 
    }

    public String getNombre() { 
        return nombre; 
    }
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }

    public Double getPrecio() { 
        return precio; 
    }
    public void setPrecio(Double precio) { 
        this.precio = precio; 
    }

    public Integer getStock() { 
        return stock; 
    }
    public void setStock(Integer stock) { 
        this.stock = stock; 
    }

    // GETTER Y SETTER CORREGIDOS PARA IMAGEN_URL
    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getDescripcion() { 
        return descripcion; 
    }
    public void setDescripcion(String descripcion) { 
        this.descripcion = descripcion; 
    }
}