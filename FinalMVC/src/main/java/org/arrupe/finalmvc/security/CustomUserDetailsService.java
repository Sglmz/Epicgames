package org.arrupe.finalmvc.security;

import org.arrupe.finalmvc.model.Usuario;
import org.arrupe.finalmvc.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        System.out.println("Intentando autenticar con correo: " + correo);

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> {
                    System.out.println("Usuario NO encontrado: " + correo);
                    return new UsernameNotFoundException("Usuario no encontrado: " + correo);
                });

        if (usuario.getRoles().isEmpty()) {
            System.out.println("Usuario sin roles asignados.");
        } else {
            System.out.println("Usuario encontrado con roles:");
            usuario.getRoles().forEach(rol ->
                    System.out.println("   - " + rol.getNombre()));
        }

        return new CustomUserDetails(usuario);
    }
}
