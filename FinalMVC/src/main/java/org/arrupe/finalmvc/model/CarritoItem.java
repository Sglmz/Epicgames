package org.arrupe.finalmvc.model;

public class CarritoItem {

    private String id;        // ID del producto como String
    private String nombre;
    private double precio;

    public CarritoItem() {
        // Constructor vacío necesario para deserialización JSON
    }

    public CarritoItem(String id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
