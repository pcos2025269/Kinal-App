package com.pablocos.KinalApp1.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
@Entity
@Table(name= "DetalleVenta")
public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long codigoDetalleVenta;
    @Column(nullable = false)
    private Long cantidad;
    @Column(nullable = false)
    private BigDecimal precioUnitario;
    @Column(nullable = false)
    private BigDecimal subTotal;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="codigo_producto",nullable = false,foreignKey = @ForeignKey(name = "FK_codigo_producto"))
    private Producto detalleproducto;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_venta",nullable = false,foreignKey = @ForeignKey(name = "FK_codigo_venta"))
    private Venta detalleVenta;

    public DetalleVenta() {
    }

    public DetalleVenta(Long codigoDetalleVenta, Long cantidad, BigDecimal precioUnitario, BigDecimal subTotal, Producto detalleproducto, Venta detalleVenta) {
        this.codigoDetalleVenta = codigoDetalleVenta;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subTotal = subTotal;
        this.detalleproducto = detalleproducto;
        this.detalleVenta = detalleVenta;
    }

    public Long getCodigoDetalleVenta() {
        return codigoDetalleVenta;
    }

    public void setCodigoDetalleVenta(Long codigoDetalleVenta) {
        this.codigoDetalleVenta = codigoDetalleVenta;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public Producto getDetalleproducto() {
        return detalleproducto;
    }

    public void setDetalleproducto(Producto detalleproducto) {
        this.detalleproducto = detalleproducto;
    }

    public Venta getDetalleVenta() {
        return detalleVenta;
    }

    public void setDetalleVenta(Venta detalleVenta) {
        this.detalleVenta = detalleVenta;
    }
}
