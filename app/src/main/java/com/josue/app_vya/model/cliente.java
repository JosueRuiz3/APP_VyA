package com.josue.app_vya.model;

public class cliente {
    String nombre_cliente, cantidad, precio_unitario, talla, total;

    public cliente() {
    }

    public cliente(String nombre_cliente, String cantidad, String precio_unitario, String talla, String total) {
        this.nombre_cliente = nombre_cliente;
        this.cantidad = cantidad;
        this.precio_unitario = precio_unitario;
        this.talla = talla;
        this.total = total;
    }

    public String getNombre_cliente() {
        return nombre_cliente;
    }

    public void setNombre_cliente(String nombre_cliente) {
        this.nombre_cliente = nombre_cliente;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(String precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
