package com.josue.app_vya.model;

public class venta {

    String nombre_producto, stock, talla, precio_compra, precio_venta;


    public venta() {
    }

    public venta(String nombre_producto, String stock, String talla, String precio_compra, String precio_venta) {
        this.nombre_producto = nombre_producto;
        this.stock = stock;
        this.talla = talla;
        this.precio_compra = precio_compra;
        this.precio_venta = precio_venta;
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

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
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
}
