package org.arrupe.finalmvc.controller;

import org.arrupe.finalmvc.model.Producto;
import org.arrupe.finalmvc.model.Usuario;
import org.arrupe.finalmvc.security.CustomUserDetails;
import org.arrupe.finalmvc.service.ProductoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/developer")
@PreAuthorize("hasRole('DEVELOPER')") // Protege todo el controlador
public class DeveloperController {

    private final ProductoService productoService;

    public DeveloperController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // Panel principal del developer con listado de productos propios
    @GetMapping("/index")
    public String mostrarPanelDesarrollador(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails userDetails) {
            Usuario usuario = userDetails.getUsuario();
            List<Producto> productos = productoService.listarPorUsuario(usuario.getIdUsuario());
            model.addAttribute("productos", productos);
        } else {
            model.addAttribute("productos", Collections.emptyList());
        }

        return "developer/index"; // siempre usa la vista principal del developer
    }

    // Guardar producto
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails userDetails) {
            Usuario usuario = userDetails.getUsuario();
            producto.setUsuario(usuario);
        }
        productoService.guardar(producto);
        return "redirect:/developer/index"; // redirige al panel developer
    }

    // Actualizar producto
    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute Producto producto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails userDetails) {
            Usuario usuario = userDetails.getUsuario();
            producto.setUsuario(usuario);
        }
        productoService.guardar(producto);
        return "redirect:/developer/index"; // redirige al panel developer
    }

    // Eliminar producto
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return "redirect:/developer/index"; // redirige al panel developer
    }

    // Ver detalle de un producto
    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        Producto producto = productoService.obtenerPorId(id);
        model.addAttribute("producto", producto);
        return "developer/detalle"; // crea la vista developer/detalle.html
    }
}
