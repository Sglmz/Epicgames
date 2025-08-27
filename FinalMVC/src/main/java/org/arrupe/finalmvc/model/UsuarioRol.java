package org.arrupe.finalmvc.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario_rol")
@IdClass(UsuarioRolId.class)
public class UsuarioRol {

    @Id
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Id
    @Column(name = "id_rol")
    private Integer idRol;

    // Relación opcional para usar con JOIN si lo necesitas
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", insertable = false, updatable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol", insertable = false, updatable = false)
    private Rol rol;

    public UsuarioRol() {}

    public UsuarioRol(Long idUsuario, Integer idRol) {
        this.idUsuario = idUsuario;
        this.idRol = idRol;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Rol getRol() {
        return rol;
    }
}
