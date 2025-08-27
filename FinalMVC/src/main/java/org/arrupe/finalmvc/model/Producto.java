package org.arrupe.finalmvc.model;

import jakarta.persistence.*;

@Entity
@Table(name = "videojuegos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_videojuego")
    private Integer id;

    private String titulo;
    private String descripcion;
    private String genero;
    private double precio;
    private double descuento;
    private String imagen;
    private boolean activo;
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    public Producto() {}

    public Producto(String titulo, String descripcion, String genero, double precio, double descuento,
                    String imagen, boolean activo, Usuario usuario) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.genero = genero;
        this.precio = precio;
        this.descuento = descuento;
        this.imagen = imagen;
        this.activo = activo;
        this.usuario = usuario;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public double getDescuento() { return descuento; }
    public void setDescuento(double descuento) { this.descuento = descuento; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", genero='" + genero + '\'' +
                ", precio=" + precio +
                ", descuento=" + descuento +
                ", imagen='" + imagen + '\'' +
                ", activo=" + activo +
                ", usuario=" + (usuario != null ? usuario.getIdUsuario() : null) +
                '}';
    }
}
