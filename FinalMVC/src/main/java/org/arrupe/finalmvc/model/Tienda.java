package org.arrupe.finalmvc.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tiendas")
public class Tienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // <- Este cambio es clave
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column
    private String direccion; // <- Asegúrate de que esté en la tabla

    @Column
    private String imagen;

    public Tienda() {
    }

    public Tienda(String nombre, String descripcion, String direccion, String imagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.imagen = imagen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
