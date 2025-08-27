package org.arrupe.finalmvc.service;

import org.arrupe.finalmvc.model.Tienda;

import java.util.List;

public interface TiendaService {
    List<Tienda> listarTodas();
    void guardar(Tienda tienda);
    void eliminar(Long id);
    Tienda buscarPorId(Long id);
}
