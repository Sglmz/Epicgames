package org.arrupe.finalmvc.repository;

import org.arrupe.finalmvc.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Buscar usuario por correo.
     */
    Optional<Usuario> findByCorreo(String correo);

    /**
     * Verifica si ya existe un username (ignora mayúsculas/minúsculas).
     */
    boolean existsByUsernameIgnoreCase(String username);

    /**
     * Verifica si ya existe un correo (ignora mayúsculas/minúsculas).
     */
    boolean existsByCorreoIgnoreCase(String correo);

    /**
     * Obtiene todos los usuarios que tengan un rol específico.
     */
    @Query(value = """
        SELECT u.* FROM usuarios u
        JOIN usuario_rol ur ON u.id_usuario = ur.id_usuario
        JOIN roles r ON ur.id_rol = r.id_rol
        WHERE r.nombre = :rol
    """, nativeQuery = true)
    List<Usuario> obtenerUsuariosPorRol(@Param("rol") String rol);
}
