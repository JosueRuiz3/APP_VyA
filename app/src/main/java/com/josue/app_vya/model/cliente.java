package com.josue.app_vya.model;

public class cliente {
    String nombre_cliente, nombre_producto, precio_unitario, descripcion, total, idVenta, fecha_entrega, fecha_pago1, fecha_pago2;
    Integer cantidad;

    public cliente() {
    }

    public cliente(String nombre_cliente, String nombre_producto, String precio_unitario, String descripcion, String total, String idVenta, String fecha_entrega, String fecha_pago1, String fecha_pago2, Integer cantidad) {
        this.nombre_cliente = nombre_cliente;
        this.nombre_producto = nombre_producto;
        this.precio_unitario = precio_unitario;
        this.descripcion = descripcion;
        this.total = total;
        this.idVenta = idVenta;
        this.fecha_entrega = fecha_entrega;
        this.fecha_pago1 = fecha_pago1;
        this.fecha_pago2 = fecha_pago2;
        this.cantidad = cantidad;
    }

    public String getNombre_cliente() {
        return nombre_cliente;
    }

    public void setNombre_cliente(String nombre_cliente) {
        this.nombre_cliente = nombre_cliente;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public String getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(String precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(String idVenta) {
        this.idVenta = idVenta;
    }

    public String getFecha_entrega() {
        return fecha_entrega;
    }

    public void setFecha_entrega(String fecha_entrega) {
        this.fecha_entrega = fecha_entrega;
    }

    public String getFecha_pago1() {
        return fecha_pago1;
    }

    public void setFecha_pago1(String fecha_pago1) {
        this.fecha_pago1 = fecha_pago1;
    }

    public String getFecha_pago2() {
        return fecha_pago2;
    }

    public void setFecha_pago2(String fecha_pago2) {
        this.fecha_pago2 = fecha_pago2;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}