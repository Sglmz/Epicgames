package org.arrupe.finalmvc.controller;

import org.arrupe.finalmvc.model.Producto;
import org.arrupe.finalmvc.model.Usuario;
import org.arrupe.finalmvc.service.ProductoService;
import org.arrupe.finalmvc.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UsuarioService usuarioService;
    private final ProductoService productoService;

    public AdminController(UsuarioService usuarioService, ProductoService productoService) {
        this.usuarioService = usuarioService;
        this.productoService = productoService;
    }

    // ✅ Vista principal del panel de administración (muestra lista de developers)
    @GetMapping("/admin")
    public String verPanelAdmin(Model model) {
        List<Usuario> developers = usuarioService.obtenerUsuariosPorRol("DEVELOPER");
        model.addAttribute("usuarios", developers);
        return "admin/admin"; // Asegúrate que este archivo existe: templates/admin/admin.html
    }

    // ✅ Ver productos desarrollados por un usuario con rol DEVELOPER
    @GetMapping("/productos/{idUsuario}")
    public String verProductosPorUsuario(@PathVariable Long idUsuario, Model model) {
        Usuario usuario = usuarioService.obtenerPorId(idUsuario);

        if (usuario == null) {
            System.out.println("❌ Usuario no encontrado con ID: " + idUsuario);
            return "redirect:/admin/admin";
        }

        String rol = usuarioService.obtenerRolPorUsuarioId(idUsuario);
        if (rol == null || !rol.equalsIgnoreCase("DEVELOPER")) {
            System.out.println("⚠️ Usuario con ID " + idUsuario + " no tiene rol DEVELOPER.");
            return "redirect:/admin/admin";
        }

        List<Producto> productos = productoService.listarPorUsuario(idUsuario);

        System.out.println("✅ Cargando productos para el usuario: " + usuario.getNombre());
        System.out.println("📦 Total productos encontrados: " + productos.size());

        model.addAttribute("usuario", usuario);
        model.addAttribute("productos", productos);

        return "admin/productos-tienda"; // Asegúrate que este archivo exista correctamente
    }
}
