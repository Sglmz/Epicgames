package org.arrupe.finalmvc.service;

import org.arrupe.finalmvc.model.Producto;
import org.arrupe.finalmvc.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // Listar todos los productos
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    // Listar productos por el ID del usuario
    public List<Producto> listarPorUsuario(Long idUsuario) {
        if (idUsuario == null) {
            System.out.println("❌ ID de usuario nulo en listarPorUsuario");
            return List.of();
        }
        List<Producto> productos = productoRepository.findByUsuarioId(idUsuario);
        System.out.println("📦 Productos encontrados para el usuario " + idUsuario + ": " + productos.size());
        return productos;
    }

    // Guardar o actualizar producto
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    // Buscar producto por ID (opcional)
    public Optional<Producto> buscar(Long id) {
        return productoRepository.findById(id);
    }

    // Obtener producto por ID (excepción si no existe)
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Producto no encontrado con ID: " + id));
    }

    // Eliminar producto por ID
    public void eliminar(Long id) {
        productoRepository.deleteById(id);
        System.out.println("🗑️ Producto eliminado con ID: " + id);
    }
}
