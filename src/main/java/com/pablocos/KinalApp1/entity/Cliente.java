package com.pablocos.KinalApp1.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "clientes")
public class Cliente {
    @Id
    @Column(name = "dpi_cliente")
    private String DPICliente;
    @Column(nullable = false)
    private String nombreCliente;
    @Column(nullable = false)
    private String apellidoCliente;
    @Column(nullable = false)
    private String direccionCliente;
    @Column(nullable = false)
    private Long estado;

    @OneToMany(mappedBy = "Venta", cascade = CascadeType.ALL)
    private List<Venta> ventas;

    public Cliente() {
    }

    public Cliente(String DPICliente, String nombreCliente, String apellidoCliente, String direccionCliente, Long estado) {
        this.DPICliente = DPICliente;
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.direccionCliente = direccionCliente;
        this.estado = estado;
    }

    public String getDPICliente() {
        return DPICliente;
    }

    public void setDPICliente(String DPICliente) {
        this.DPICliente = DPICliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getApellidoCliente() {
        return apellidoCliente;
    }

    public void setApellidoCliente(String apellidoCliente) {
        this.apellidoCliente = apellidoCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }
}
