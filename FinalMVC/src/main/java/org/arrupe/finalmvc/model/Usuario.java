package org.arrupe.finalmvc.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    // En el SQL es NOT NULL y UNIQUE
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    // En el SQL es NOT NULL y UNIQUE
    @Column(name = "correo", nullable = false, unique = true)
    private String correo;

    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    // Nuevo campo de saldo (la columna la agregaste en BD como DOUBLE)
    @Column(name = "dinero", nullable = false)
    private Double dinero = 0.0;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_rol",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_rol")
    )
    private Set<Rol> roles = new HashSet<>();

    // --- Constructores ---
    public Usuario() {}

    public Usuario(String username, String nombre, String apellido, String correo, String contrasena, Double dinero) {
        this.username = username;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasena = contrasena;
        this.dinero = dinero != null ? dinero : 0.0;
    }

    // --- Getters y Setters ---
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public Double getDinero() { return dinero; }
    public void setDinero(Double dinero) { this.dinero = dinero != null ? dinero : 0.0; }

    public Set<Rol> getRoles() { return roles; }
    public void setRoles(Set<Rol> roles) { this.roles = roles != null ? roles : new HashSet<>(); }

    // --- Helpers de roles ---
    public void agregarRol(Rol rol) { if (rol != null) this.roles.add(rol); }
    public void limpiarRoles() { this.roles.clear(); }

    // --- Helpers de saldo ---
    public void acreditar(Double monto) {
        if (monto == null || monto < 0) throw new IllegalArgumentException("Monto inválido");
        this.dinero += monto;
    }

    public void debitar(Double monto) {
        if (monto == null || monto < 0) throw new IllegalArgumentException("Monto inválido");
        if (this.dinero < monto) throw new IllegalStateException("Saldo insuficiente");
        this.dinero -= monto;
    }

    // --- Para autenticación si usas Spring Security manualmente ---
    public String getPassword() { return contrasena; }
    // Si autenticas por username, deja getUsername() como está;
    // si autenticas por correo, expón un alias:
    public String getEmailAsUsername() { return correo; }

    // --- equals/hashCode por id ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(idUsuario, usuario.idUsuario);
    }
    @Override
    public int hashCode() { return Objects.hash(idUsuario); }

    @Override
    public String toString() {
        return "Usuario{id=" + idUsuario + ", username='" + username + "', correo='" + correo + "'}";
    }
}
