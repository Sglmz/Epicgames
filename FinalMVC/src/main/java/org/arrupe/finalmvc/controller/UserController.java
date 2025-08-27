package org.arrupe.finalmvc.controller;

import jakarta.servlet.http.HttpSession;
import org.arrupe.finalmvc.model.Producto;
import org.arrupe.finalmvc.model.Usuario;
import org.arrupe.finalmvc.model.VideojuegoComprado;
import org.arrupe.finalmvc.security.CustomUserDetails;
import org.arrupe.finalmvc.service.ProductoService;
import org.arrupe.finalmvc.service.UsuarioService;
import org.arrupe.finalmvc.repository.VideojuegoCompradoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private VideojuegoCompradoRepository videojuegoCompradoRepository;

    @GetMapping("/index")
    public String verCatalogoJuegos(Model model, HttpSession session) {
        var productos = productoService.listarTodos();
        model.addAttribute("productos", productos);

        Usuario usuario = (Usuario) session.getAttribute("usuarioActual");
        Set<Integer> idsComprados = new HashSet<>();

        if (usuario != null) {
            usuario = usuarioService.buscarPorCorreo(usuario.getCorreo()).orElse(usuario);
            session.setAttribute("usuarioActual", usuario);
            model.addAttribute("saldo", usuario.getDinero());
            model.addAttribute("username", usuario.getUsername());

            List<VideojuegoComprado> compras = videojuegoCompradoRepository.findByUsuario(usuario);
            idsComprados = compras.stream()
                    .map(c -> c.getVideojuego().getId())
                    .collect(Collectors.toSet());
        } else {
            model.addAttribute("saldo", 0.0);
            model.addAttribute("username", "Invitado");
        }

        model.addAttribute("comprados", idsComprados);
        return "user/index";
    }

    @GetMapping("/abonar")
    public String mostrarFormularioAbono(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioActual");
        if (usuario != null) {
            model.addAttribute("username", usuario.getUsername());
            model.addAttribute("saldo", usuario.getDinero());
        }
        return "user/abonar";
    }

    @PostMapping("/abonar")
    public String procesarAbono(@RequestParam("monto") Double monto,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioActual");

        List<Double> montosValidos = Arrays.asList(5.0, 10.0, 20.0, 50.0, 100.0, 200.0);
        if (usuario == null || !montosValidos.contains(monto)) {
            redirectAttributes.addFlashAttribute("error", "Datos inválidos o sesión no iniciada.");
            return "redirect:/user/abonar";
        }

        usuario.acreditar(monto);
        usuarioService.actualizar(usuario);
        session.setAttribute("usuarioActual", usuario);

        redirectAttributes.addFlashAttribute("mensaje", "Abono exitoso. Saldo actualizado.");
        return "redirect:/user/index";
    }

    @GetMapping("/biblioteca")
    public String verBiblioteca(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioActual");
        if (usuario == null) {
            return "redirect:/user/index";
        }

        usuario = usuarioService.buscarPorCorreo(usuario.getCorreo()).orElse(usuario);
        session.setAttribute("usuarioActual", usuario);

        List<VideojuegoComprado> compras = videojuegoCompradoRepository.findByUsuario(usuario);
        List<Producto> juegosComprados = compras.stream()
                .map(VideojuegoComprado::getVideojuego)
                .collect(Collectors.toList());

        model.addAttribute("juegosComprados", juegosComprados);
        model.addAttribute("username", usuario.getUsername());
        return "user/biblioteca";
    }

    @GetMapping("/cambiarRolDeveloper")
    public String cambiarARolDeveloper(HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioActual");

        if (usuario != null) {
            usuarioService.cambiarRol(usuario.getIdUsuario(), 2); // Cambia en BD al rol DEVELOPER

            List<SimpleGrantedAuthority> nuevaAutoridad = List.of(new SimpleGrantedAuthority("ROLE_DEVELOPER"));
            Authentication nuevaAuth = new UsernamePasswordAuthenticationToken(
                    new CustomUserDetails(usuario), null, nuevaAutoridad
            );
            SecurityContextHolder.getContext().setAuthentication(nuevaAuth);

            session.setAttribute("usuarioActual", usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Modo desarrollador activado.");
            return "redirect:/developer/index";
        }

        redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión.");
        return "redirect:/login";
    }

    @GetMapping("/volverARolUsuario")
    public String volverARolUsuario(HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioActual");

        if (usuario != null) {
            usuarioService.cambiarRol(usuario.getIdUsuario(), 3); // Cambia en BD al rol USER

            List<SimpleGrantedAuthority> nuevaAutoridad = List.of(new SimpleGrantedAuthority("ROLE_USER"));
            Authentication nuevaAuth = new UsernamePasswordAuthenticationToken(
                    new CustomUserDetails(usuario), null, nuevaAutoridad
            );
            SecurityContextHolder.getContext().setAuthentication(nuevaAuth);

            session.setAttribute("usuarioActual", usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Has vuelto al modo usuario.");
            return "redirect:/user/index";
        }

        redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión.");
        return "redirect:/login";
    }
}
