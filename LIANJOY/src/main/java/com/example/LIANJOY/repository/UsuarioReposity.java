package com.example.LIANJOY.repository;

import com.example.LIANJOY.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioReposity extends JpaRepository<Usuario, Integer> {
    
    // Este método te servirá para el Login después
    Optional<Usuario> findByEmail(String email);
}