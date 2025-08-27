package org.arrupe.finalmvc.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.arrupe.finalmvc.model.CarritoItem;
import org.arrupe.finalmvc.model.Producto;
import org.arrupe.finalmvc.model.Usuario;
import org.arrupe.finalmvc.service.CarritoService;
import org.arrupe.finalmvc.service.ProductoService;
import org.arrupe.finalmvc.service.UsuarioService;
import org.arrupe.finalmvc.service.VideojuegoCompradoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    private final CarritoService carritoService;
    private final UsuarioService usuarioService;
    private final VideojuegoCompradoService videojuegoCompradoService;
    private final ProductoService productoService;

    public CarritoController(CarritoService carritoService,
                             UsuarioService usuarioService,
                             VideojuegoCompradoService videojuegoCompradoService,
                             ProductoService productoService) {
        this.carritoService = carritoService;
        this.usuarioService = usuarioService;
        this.videojuegoCompradoService = videojuegoCompradoService;
        this.productoService = productoService;
    }

    @PostMapping("/pagar")
    public String pagar(@RequestParam("items") String itemsJson,
                        HttpSession session,
                        Principal principal,
                        RedirectAttributes redirectAttrs) {
        try {
            // Verificar si el usuario está autenticado
            if (principal == null) return "redirect:/login";

            Usuario usuario = usuarioService.buscarPorCorreo(principal.getName()).orElse(null);
            if (usuario == null) return "redirect:/login";

            // Convertir el JSON recibido en una lista de ítems del carrito
            ObjectMapper mapper = new ObjectMapper();
            List<CarritoItem> carrito = mapper.readValue(itemsJson, new TypeReference<>() {});
            double total = carrito.stream().mapToDouble(CarritoItem::getPrecio).sum();

            if (usuario.getDinero() < total) {
                redirectAttrs.addFlashAttribute("error", "Saldo insuficiente.");
                return "redirect:/user/index";
            }

            // Descontar el saldo y redondear a 2 decimales
            double nuevoSaldo = Math.round((usuario.getDinero() - total) * 100.0) / 100.0;
            usuario.setDinero(nuevoSaldo);
            usuarioService.actualizar(usuario);
            session.setAttribute("usuarioActual", usuario); // actualizar sesión

            // Registrar cada producto comprado
            for (CarritoItem item : carrito) {
                Producto producto = productoService.obtenerPorId(Long.valueOf(item.getId()));
                if (producto != null) {
                    videojuegoCompradoService.registrarCompra(usuario, producto);
                }
            }

            redirectAttrs.addFlashAttribute("success", "Compra realizada con éxito.");
            return "redirect:/user/index";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttrs.addFlashAttribute("error", "Error al procesar la compra.");
            return "redirect:/user/index";
        }
    }
}
