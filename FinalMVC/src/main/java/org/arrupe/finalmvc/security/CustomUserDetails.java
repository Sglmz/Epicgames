package org.arrupe.finalmvc.security;

import org.arrupe.finalmvc.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final Usuario usuario;

    public CustomUserDetails(Usuario usuario) {
        this.usuario = usuario;
        System.out.println("🟢 CustomUserDetails creado para: " + usuario.getCorreo());
    }
    public Usuario getUsuario() {
        return usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var authorities = usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre()))
                .collect(Collectors.toList());

        System.out.println("Autoridades del usuario:");
        authorities.forEach(a -> System.out.println("   -> " + a.getAuthority()));

        return authorities;
    }

    @Override
    public String getPassword() {
        System.out.println("Password (en BD): " + usuario.getContrasena());
        return usuario.getContrasena();
    }

    @Override
    public String getUsername() {
        return usuario.getCorreo();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
