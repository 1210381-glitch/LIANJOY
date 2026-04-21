package com.example.LIANJOY.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Data;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String email;
    private String password;
    private String telefono;

    @Column(name = "fechas_registro")
    private LocalDate fechaRegistro = LocalDate.now();

    // Getters y Setters
}