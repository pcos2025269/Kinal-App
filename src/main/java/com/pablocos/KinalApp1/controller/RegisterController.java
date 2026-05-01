package com.pablocos.KinalApp1.controller;

import com.pablocos.KinalApp1.entity.Usuario;
import com.pablocos.KinalApp1.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@RequestParam String nameUser, @RequestParam String passwordUser, @RequestParam String email) {

        if (repository.findByNameUser(nameUser).isPresent()) {
            return "redirect:/registro?error=existe";
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNameUser(nameUser);
        nuevoUsuario.setPasswordUser(passwordEncoder.encode(passwordUser));
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setEstado(1L);
        nuevoUsuario.setRol("usuario");

        repository.save(nuevoUsuario);

        return "redirect:/login?success=registrado";
    }

}
