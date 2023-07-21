package com.josue.app_vya.model;

import com.google.firebase.Timestamp;

import java.util.Date;

public class venta {

    String nombre_producto, stock, descripcion, precio_compra, precio_venta ;
    Timestamp fecha_creacion;

    public venta() {
    }

    public venta(String nombre_producto, String stock, String descripcion, String precio_compra, String precio_venta, Timestamp fecha_creacion) {
        this.nombre_producto = nombre_producto;
        this.stock = stock;
        this.descripcion = descripcion;
        this.precio_compra = precio_compra;
        this.precio_venta = precio_venta;
        this.fecha_creacion = fecha_creacion;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio_compra() {
        return precio_compra;
    }

    public void setPrecio_compra(String precio_compra) {
        this.precio_compra = precio_compra;
    }

    public String getPrecio_venta() {
        return precio_venta;
    }

    public void setPrecio_venta(String precio_venta) {
        this.precio_venta = precio_venta;
    }

    public Timestamp getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Timestamp fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }
}
