package org.arrupe.finalmvc.controller;

import org.arrupe.finalmvc.model.Producto;
import org.arrupe.finalmvc.model.Usuario;
import org.arrupe.finalmvc.security.CustomUserDetails;
import org.arrupe.finalmvc.service.ProductoService;
import org.arrupe.finalmvc.service.UsuarioService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/productos")
@PreAuthorize("hasAnyRole('ADMIN','DEVELOPER')")
public class ProductoController {

    private final ProductoService productoService;
    private final UsuarioService usuarioService;

    public ProductoController(ProductoService productoService, UsuarioService usuarioService) {
        this.productoService = productoService;
        this.usuarioService = usuarioService;
    }

    // Mostrar formulario de creación
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(
            @RequestParam(name = "origen", required = false) String origen,
            @RequestParam(name = "idUsuario", required = false) Long idUsuario,
            Model model) {

        Producto producto = new Producto();

        if (idUsuario != null) {
            Usuario usuario = usuarioService.obtenerPorId(idUsuario);
            producto.setUsuario(usuario);
        }

        model.addAttribute("producto", producto);
        model.addAttribute("origen", origen);
        model.addAttribute("idUsuario", idUsuario);

        return "productos/agregar";
    }

    // Guardar producto
    @PostMapping("/guardar")
    public String guardar(
            @ModelAttribute Producto producto,
            @RequestParam(name = "origen", required = false) String origen,
            @RequestParam(name = "idUsuario", required = false) Long idUsuario) {

        if (producto.getUsuario() == null || producto.getUsuario().getIdUsuario() == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof CustomUserDetails userDetails) {
                Usuario usuario = userDetails.getUsuario();
                producto.setUsuario(usuario);
            }
        }

        productoService.guardar(producto);

        if ("admin".equalsIgnoreCase(origen) && idUsuario != null) {
            return "redirect:/admin/productos/" + idUsuario;
        } else if ("developer".equalsIgnoreCase(origen)) {
            return "redirect:/developer/index";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) && idUsuario != null) {
            return "redirect:/admin/productos/" + idUsuario;
        }

        return "redirect:/developer/index";
    }

    // Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id,
                                          @RequestParam(name = "origen", required = false) String origen,
                                          @RequestParam(name = "idUsuario", required = false) Long idUsuario,
                                          Model model) {

        productoService.buscar(id).ifPresent(p -> model.addAttribute("producto", p));
        model.addAttribute("origen", origen);
        model.addAttribute("idUsuario", idUsuario);

        return "productos/editar";
    }

    // Actualizar producto
    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute Producto producto,
                             @RequestParam(name = "origen", required = false) String origen,
                             @RequestParam(name = "idUsuario", required = false) Long idUsuario) {

        Producto existente = productoService.buscar(producto.getId().longValue()).orElse(null);

        if (existente != null) {
            producto.setUsuario(existente.getUsuario());
            productoService.guardar(producto);
        }

        if ("admin".equalsIgnoreCase(origen) && idUsuario != null) {
            return "redirect:/admin/productos/" + idUsuario;
        } else if ("developer".equalsIgnoreCase(origen)) {
            return "redirect:/developer/index";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) && idUsuario != null) {
            return "redirect:/admin/productos/" + idUsuario;
        }

        return "redirect:/developer/index";
    }

    // Eliminar producto
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id,
                           @RequestParam(name = "origen", required = false) String origen,
                           @RequestParam(name = "idUsuario", required = false) Long idUsuario) {

        productoService.eliminar(id);

        if ("admin".equalsIgnoreCase(origen) && idUsuario != null) {
            return "redirect:/admin/productos/" + idUsuario;
        } else if ("developer".equalsIgnoreCase(origen)) {
            return "redirect:/developer/index";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) && idUsuario != null) {
            return "redirect:/admin/productos/" + idUsuario;
        }

        return "redirect:/developer/index";
    }

    // Ver detalle del producto
    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        Producto producto = productoService.obtenerPorId(id);
        model.addAttribute("producto", producto);
        return "productos/detalle";
    }
}
