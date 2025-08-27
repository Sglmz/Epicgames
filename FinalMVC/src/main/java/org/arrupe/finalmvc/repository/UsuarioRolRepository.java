package org.arrupe.finalmvc.repository;

import jakarta.transaction.Transactional;
import org.arrupe.finalmvc.model.UsuarioRol;
import org.arrupe.finalmvc.model.UsuarioRolId;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, UsuarioRolId> {

    @Modifying
    @Transactional
    @Query("DELETE FROM UsuarioRol ur WHERE ur.idUsuario = :idUsuario")
    void eliminarRolesPorUsuario(@Param("idUsuario") Long idUsuario);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO usuario_rol (id_usuario, id_rol) VALUES (:idUsuario, :idRol)", nativeQuery = true)
    void insertarNuevoRol(@Param("idUsuario") Long idUsuario, @Param("idRol") int idRol);

    @Query("SELECT ur.rol.nombre FROM UsuarioRol ur WHERE ur.idUsuario = :idUsuario")
    Optional<String> findRolNombreByUsuarioId(@Param("idUsuario") Long idUsuario);
}
