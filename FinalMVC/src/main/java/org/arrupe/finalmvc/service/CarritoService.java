package org.arrupe.finalmvc.service;

import jakarta.servlet.http.HttpSession;
import org.arrupe.finalmvc.model.CarritoItem;
import org.arrupe.finalmvc.model.Producto;
import org.arrupe.finalmvc.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarritoService {

    private final ProductoRepository productoRepository;

    public CarritoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public void agregarProducto(HttpSession session, Long productoId) {
        Producto producto = productoRepository.findById(productoId).orElse(null);

        if (producto != null) {
            double precioFinal = producto.getPrecio() - (producto.getPrecio() * producto.getDescuento());

            List<CarritoItem> carrito = obtenerCarrito(session);
            carrito.add(new CarritoItem(productoId.toString(), producto.getTitulo(), precioFinal));

            session.setAttribute("carrito", carrito);
        }
    }

    @SuppressWarnings("unchecked")
    public List<CarritoItem> obtenerCarrito(HttpSession session) {
        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
            session.setAttribute("carrito", carrito);
        }
        return carrito;
    }

    public void limpiarCarrito(HttpSession session) {
        session.removeAttribute("carrito");
    }

    public double calcularTotal(HttpSession session) {
        return obtenerCarrito(session).stream().mapToDouble(CarritoItem::getPrecio).sum();
    }
}
