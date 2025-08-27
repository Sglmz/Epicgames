package org.arrupe.finalmvc.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.arrupe.finalmvc.model.Usuario;
import org.arrupe.finalmvc.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UsuarioService usuarioService;

    public CustomLoginSuccessHandler(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // 🔐 Obtener datos básicos desde UserDetails
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Usuario usuario = userDetails.getUsuario();

        // 🔄 Refrescar el usuario desde BD (para traer saldo, compras, etc.)
        Usuario usuarioCompleto = usuarioService.buscarPorCorreo(usuario.getCorreo())
                .orElse(usuario);

        // ✅ Guardar usuario completo en sesión
        request.getSession().setAttribute("usuarioActual", usuarioCompleto);

        // 🔄 Redirigir según el rol
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String rol = authority.getAuthority();

            System.out.println("Redirigiendo según rol: " + rol);

            if (rol.equals("ROLE_ADMIN")) {
                response.sendRedirect("/admin/admin");
                return;
            } else if (rol.equals("ROLE_DEVELOPER")) {
                response.sendRedirect("/developer/index");
                return;
            } else if (rol.equals("ROLE_USER")) {
                response.sendRedirect("/user/index");
                return;
            }
        }

        // Si no tiene rol reconocido
        response.sendRedirect("/login?error=role");
    }
}
