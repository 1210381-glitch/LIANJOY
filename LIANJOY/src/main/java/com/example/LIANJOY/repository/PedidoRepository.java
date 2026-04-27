package com.example.LIANJOY.repository;

import com.example.LIANJOY.model.Pedido; // Importa tu entidad Pedido
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List; // Importa la utilidad de Listas

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    
    // Este método busca pedidos filtrando por el ID del usuario
    List<Pedido> findByUsuarioId(Integer usuarioId);
}