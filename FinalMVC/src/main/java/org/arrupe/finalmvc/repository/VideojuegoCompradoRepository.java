package org.arrupe.finalmvc.repository;

import org.arrupe.finalmvc.model.VideojuegoComprado;
import org.arrupe.finalmvc.model.Usuario;
import org.arrupe.finalmvc.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideojuegoCompradoRepository extends JpaRepository<VideojuegoComprado, Long> {
    boolean existsByUsuarioAndVideojuego(Usuario usuario, Producto videojuego);
    List<VideojuegoComprado> findByUsuario(Usuario usuario);
}
