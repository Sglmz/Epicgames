package org.arrupe.finalmvc.model;

import java.io.Serializable;
import java.util.Objects;

public class UsuarioRolId implements Serializable {

    private Long idUsuario;
    private Integer idRol;

    public UsuarioRolId() {}

    public UsuarioRolId(Long idUsuario, Integer idRol) {
        this.idUsuario = idUsuario;
        this.idRol = idRol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioRolId that)) return false;
        return Objects.equals(idUsuario, that.idUsuario) &&
                Objects.equals(idRol, that.idRol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario, idRol);
    }
}
