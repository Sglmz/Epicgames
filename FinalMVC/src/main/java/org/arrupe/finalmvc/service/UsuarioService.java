package org.arrupe.finalmvc.service;

import org.arrupe.finalmvc.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    Optional<Usuario> buscarPorCorreo(String correo);

    Optional<Usuario> buscarPorCorreoYContrasena(String correo, String contrasena);

    void abonarDinero(Long idUsuario, Double monto);

    void actualizar(Usuario usuario);

    void cambiarRol(Long idUsuario, int nuevoIdRol);

    String obtenerRolPorUsuarioId(Long idUsuario);

    Usuario obtenerPorId(Long idUsuario);

    List<Usuario> obtenerUsuariosPorRol(String nombreRol);
    Usuario registrarConRolPorDefecto(Usuario usuario);

    boolean existeUsernameOCorreo(String username, String correo);
}
