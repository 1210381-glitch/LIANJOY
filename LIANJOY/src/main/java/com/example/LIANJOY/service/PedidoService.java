package com.example.LIANJOY.service;

import com.example.LIANJOY.model.*;
import com.example.LIANJOY.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoService {

    @Autowired private PedidoRepository pedidoRepo;
    @Autowired private DetallePedidoRepository detalleRepo;

    @Transactional
    public Pedido crearPedido(Usuario usuario, Producto producto, Integer cantidad) {
        // 1. Crear la cabecera del pedido
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setTotal(producto.getPrecio() * cantidad);
        pedido = pedidoRepo.save(pedido);

        // 2. Crear el detalle
        DetallePedido detalle = new DetallePedido();
        detalle.setPedido(pedido);
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(producto.getPrecio());
        detalleRepo.save(detalle);

        return pedido;
    }
}