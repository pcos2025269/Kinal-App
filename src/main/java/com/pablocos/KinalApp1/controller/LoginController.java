package com.pablocos.KinalApp1.controller;

import com.pablocos.KinalApp1.entity.Usuario;
import com.pablocos.KinalApp1.repository.UsuarioRepository;
import com.pablocos.KinalApp1.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @Autowired
    private final IUsuarioService usuarioService;

    @Autowired
    UsuarioRepository repository;

    public LoginController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String index(){return "login";}

    @PostMapping("/login")
    public String login(@RequestParam String nameUser,
                        @RequestParam String passwordUser) {
        boolean isAuth = usuarioService.validarAcceso(nameUser, passwordUser);
        if (isAuth) {
            return "redirect:/Inicio";
        } else {
            return "redirect:/login?error=true";
        }
    }

    @GetMapping("/Inicio")
    public String Inicio() {
        return "Inicio"; // Asegúrate de tener un dashboard.html o cambia la ruta
    }
}
