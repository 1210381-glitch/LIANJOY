package com.example.LIANJOY.repository;

import com.example.LIANJOY.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository 
// Verifica que diga JpaRepository<DetallePedido, Integer>
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Integer> {
}