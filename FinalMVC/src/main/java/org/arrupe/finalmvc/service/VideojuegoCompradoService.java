package org.arrupe.finalmvc.service;

import org.arrupe.finalmvc.model.Usuario;
import org.arrupe.finalmvc.model.Producto;
import org.arrupe.finalmvc.model.VideojuegoComprado;
import org.arrupe.finalmvc.repository.VideojuegoCompradoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideojuegoCompradoService {

    @Autowired
    private VideojuegoCompradoRepository repo;

    public void registrarCompra(Usuario usuario, Producto producto) {
        if (!repo.existsByUsuarioAndVideojuego(usuario, producto)) {
            VideojuegoComprado compra = new VideojuegoComprado();
            compra.setUsuario(usuario);
            compra.setVideojuego(producto);
            repo.save(compra);
        }
    }
}
