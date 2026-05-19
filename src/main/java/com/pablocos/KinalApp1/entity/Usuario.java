package com.pablocos.KinalApp1.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Usuarios" )
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(nullable = false)
    private String nameUser;
    @Column(nullable = false)
    private String passwordUser;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String rol;
    @Column(nullable = false)
    private Long estado;

    @OneToMany(mappedBy = "usuarioVenta",cascade = CascadeType.ALL)
    private List<Venta> venta;

    public Usuario() {
    }

    public Usuario(Long id, String nameUser, String passwordUser, String email, String rol, Long estado, List<Venta> venta) {
        this.id = id;
        this.nameUser = nameUser;
        this.passwordUser = passwordUser;
        this.email = email;
        this.rol = rol;
        this.estado = estado;
        this.venta = venta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getPasswordUser() {
        return passwordUser;
    }

    public void setPasswordUser(String passwordUser) {
        this.passwordUser = passwordUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    public List<Venta> getVenta() {
        return venta;
    }

    public void setVenta(List<Venta> venta) {
        this.venta = venta;
    }
}
