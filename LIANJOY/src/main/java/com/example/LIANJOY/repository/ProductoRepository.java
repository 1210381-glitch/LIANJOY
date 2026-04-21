package com.example.LIANJOY.repository;

import com.example.LIANJOY.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    // Aquí puedes agregar métodos personalizados si los necesitas después, 
    // por ejemplo, para buscar productos por categoría:
    // List<Producto> findByCategoriasId(Integer categoriaId);
}