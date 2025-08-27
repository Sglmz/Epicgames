package org.arrupe.finalmvc.service;

import org.arrupe.finalmvc.model.Tienda;
import org.arrupe.finalmvc.repository.TiendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TiendaServiceImpl implements TiendaService {

    private final TiendaRepository tiendaRepository;

    @Autowired
    public TiendaServiceImpl(TiendaRepository tiendaRepository) {
        this.tiendaRepository = tiendaRepository;
    }

    @Override
    public List<Tienda> listarTodas() {
        return tiendaRepository.findAll();
    }

    @Override
    public void guardar(Tienda tienda) {
        tiendaRepository.save(tienda);
    }

    @Override
    public void eliminar(Long id) {
        tiendaRepository.deleteById(id);
    }

    @Override
    public Tienda buscarPorId(Long id) {
        Optional<Tienda> tienda = tiendaRepository.findById(id);
        return tienda.orElse(null);
    }
}
