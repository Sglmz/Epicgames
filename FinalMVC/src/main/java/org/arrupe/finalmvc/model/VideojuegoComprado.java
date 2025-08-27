package org.arrupe.finalmvc.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "videojuegos_comprados")
public class VideojuegoComprado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_videojuego")
    private Producto videojuego;

    @Column(name = "fecha_compra")
    private LocalDateTime fechaCompra = LocalDateTime.now();

    // Getters y Setters
    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Producto getVideojuego() { return videojuego; }
    public void setVideojuego(Producto videojuego) { this.videojuego = videojuego; }
    public LocalDateTime getFechaCompra() { return fechaCompra; }
    public void setFechaCompra(LocalDateTime fechaCompra) { this.fechaCompra = fechaCompra; }
}
