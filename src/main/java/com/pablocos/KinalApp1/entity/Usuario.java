package com.pablocos.KinalApp1.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity

@Table(name = "Usuarios" )
public class Usuario {
    @Id
    @Column(name = "codigo_Usuario", nullable = false)
    private Long codigoUsuario;
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

    @OneToMany(mappedBy = "Venta",cascade = CascadeType.ALL)
    private List<Venta> venta;

    public Usuario() {
    }

    public Usuario(Long codigoUsuario, String nameUser, String passwordUser, String email, String rol, Long estado) {
        this.codigoUsuario = codigoUsuario;
        this.nameUser = nameUser;
        this.passwordUser = passwordUser;
        this.email = email;
        this.rol = rol;
        this.estado = estado;
    }

    public Long getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(Long codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
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
}
