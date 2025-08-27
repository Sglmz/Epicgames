package org.arrupe.finalmvc.controller;

import jakarta.servlet.http.HttpSession;
import org.arrupe.finalmvc.model.Usuario;
import org.arrupe.finalmvc.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class HomeController {

    private final UsuarioService usuarioService;

    public HomeController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Redirige a /login si alguien entra a /
    @GetMapping("/")
    public String redirigir() {
        return "redirect:/login";
    }

    // Muestra formulario de login (templates/login.html)
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    // Procesa login
    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam(value = "correo", required = false) String correo,
            @RequestParam(value = "contrasena", required = false) String contrasena,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if ((correo == null || correo.isBlank()) && username != null && !username.isBlank()) {
            correo = username; // ⚠️ si username ≠ correo, debes implementar búsqueda por username
        }
        if ((contrasena == null || contrasena.isBlank()) && password != null && !password.isBlank()) {
            contrasena = password;
        }

        if (correo == null || contrasena == null || correo.isBlank() || contrasena.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Debes ingresar tus credenciales.");
            return "redirect:/login";
        }

        Optional<Usuario> usuarioOpt = usuarioService.buscarPorCorreoYContrasena(correo, contrasena);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            Usuario usuarioCompleto = usuarioService.buscarPorCorreo(usuario.getCorreo()).orElse(usuario);

            session.setAttribute("usuarioActual", usuarioCompleto);

            return "redirect:/user/index";
        } else {
            redirectAttributes.addFlashAttribute("error", "Correo o contraseña incorrectos.");
            return "redirect:/login";
        }
    }

    // Formulario de registro
    @GetMapping("/register")
    public String mostrarRegistro() {
        return "register"; // templates/register.html
    }

    // Procesa registro
    @PostMapping("/register")
    public String procesarRegistro(@RequestParam String username,
                                   @RequestParam String correo,
                                   @RequestParam String nombre,
                                   @RequestParam(required = false) String apellido,
                                   @RequestParam String password,
                                   @RequestParam String confirm,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        if (!password.equals(confirm)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "register";
        }
        if (usuarioService.existeUsernameOCorreo(username, correo)) {
            model.addAttribute("error", "Usuario o correo ya está en uso");
            return "register";
        }

        Usuario u = new Usuario();
        u.setUsername(username);
        u.setCorreo(correo);
        u.setNombre(nombre);
        u.setApellido(apellido);
        u.setContrasena(password);
        u.setDinero(0.0);

        usuarioService.registrarConRolPorDefecto(u);

        redirectAttributes.addFlashAttribute("success", "Cuenta creada correctamente. Ahora inicia sesión.");
        return "redirect:/login?registered=1";
    }

    // Cerrar sesión
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login?logout";
    }
}
