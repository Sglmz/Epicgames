package org.arrupe.finalmvc.service;

import jakarta.transaction.Transactional;
import org.arrupe.finalmvc.model.Usuario;
import org.arrupe.finalmvc.model.UsuarioRol;
import org.arrupe.finalmvc.repository.UsuarioRepository;
import org.arrupe.finalmvc.repository.UsuarioRolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioRolRepository usuarioRolRepository;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              UsuarioRolRepository usuarioRolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioRolRepository = usuarioRolRepository;
    }

    @Override
    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    @Override
    public Optional<Usuario> buscarPorCorreoYContrasena(String correo, String contrasena) {
        return usuarioRepository.findByCorreo(correo)
                .filter(usuario -> usuario.getContrasena().equals(contrasena));
    }

    @Override
    public void abonarDinero(Long idUsuario, Double monto) {
        usuarioRepository.findById(idUsuario).ifPresent(usuario -> {
            usuario.acreditar(monto);
            usuarioRepository.save(usuario);
        });
    }

    @Override
    public void actualizar(Usuario usuario) {
        if (usuario != null) {
            usuarioRepository.save(usuario);
        }
    }

    @Override
    @Transactional
    public void cambiarRol(Long idUsuario, int nuevoIdRol) {
        usuarioRolRepository.eliminarRolesPorUsuario(idUsuario);
        usuarioRolRepository.insertarNuevoRol(idUsuario, nuevoIdRol);
    }

    @Override
    public String obtenerRolPorUsuarioId(Long idUsuario) {
        return usuarioRolRepository.findRolNombreByUsuarioId(idUsuario)
                .orElse("SIN_ROL");
    }

    @Override
    public Usuario obtenerPorId(Long idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));
    }

    @Override
    public List<Usuario> obtenerUsuariosPorRol(String rol) {
        return usuarioRepository.obtenerUsuariosPorRol(rol);
    }

    /**
     * ✅ Crea usuario y le asigna rol 3 (USER) por defecto.
     */
    @Override
    @Transactional
    public Usuario registrarConRolPorDefecto(Usuario usuario) {
        Usuario guardado = usuarioRepository.save(usuario);

        UsuarioRol ur = new UsuarioRol();
        ur.setIdUsuario(guardado.getIdUsuario());
        ur.setIdRol(3); // rol USER por defecto

        usuarioRolRepository.save(ur);
        return guardado;
    }

    @Override
    public boolean existeUsernameOCorreo(String username, String correo) {
        return usuarioRepository.existsByUsernameIgnoreCase(username)
                || usuarioRepository.existsByCorreoIgnoreCase(correo);
    }
}
