package com.pablocos.KinalApp1.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
@Entity
@Table(name = "Ventas")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_Venta", nullable = false)
    private Long codigoVenta;
    @Column(nullable = false)
    private LocalDate fechaVenta;
    @Column(nullable = false)
    private BigDecimal total;
    @Column(nullable = false)
    private Long estado;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="dpi_cliente",nullable = false,foreignKey = @ForeignKey(name = "FK_dpi_cliente"))
    private Cliente clienteVenta;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="codigo_usuario",nullable = false,foreignKey = @ForeignKey(name = "FK_codigo_usuario"))
    private Usuario usuarioVenta;

    @OneToMany(mappedBy = "detalleVenta", cascade = CascadeType.ALL)
    private List<DetalleVenta> detalleVenta;

    public Venta() {
    }

    public Venta(Long codigoVenta, LocalDate fechaVenta, BigDecimal total, Long estado, Cliente clienteVenta, Usuario usuarioVenta) {
        this.codigoVenta = codigoVenta;
        this.fechaVenta = fechaVenta;
        this.total = total;
        this.estado = estado;
        this.clienteVenta = clienteVenta;
        this.usuarioVenta = usuarioVenta;
    }

    public Long getCodigoVenta() {
        return codigoVenta;
    }

    public void setCodigoVenta(Long codigoVenta) {
        this.codigoVenta = codigoVenta;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDate fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    public Cliente getClienteVenta() {
        return clienteVenta;
    }

    public void setClienteVenta(Cliente clienteVenta) {
        this.clienteVenta = clienteVenta;
    }

    public Usuario getUsuarioVenta() {
        return usuarioVenta;
    }

    public void setUsuarioVenta(Usuario usuarioVenta) {
        this.usuarioVenta = usuarioVenta;
    }
}
