package org.arrupe.finalmvc.controller;

import org.arrupe.finalmvc.model.Producto;
import org.arrupe.finalmvc.repository.ProductoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoRestController {

    private final ProductoRepository productoRepository;

    public ProductoRestController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @GetMapping
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        Optional<Producto> producto = productoRepository.findById(id);
        return producto.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Producto crear(@RequestBody Producto producto) {
        return productoRepository.save(producto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto datos) {
        return productoRepository.findById(id)
                .map(p -> {
                    p.setTitulo(datos.getTitulo());
                    p.setDescripcion(datos.getDescripcion());
                    p.setGenero(datos.getGenero());
                    p.setPrecio(datos.getPrecio());
                    p.setImagen(datos.getImagen());
                    p.setActivo(datos.isActivo());
                    return ResponseEntity.ok(productoRepository.save(p));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
