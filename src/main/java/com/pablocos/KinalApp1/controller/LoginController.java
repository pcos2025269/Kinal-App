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

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }


    @GetMapping("/Inicio")
    public String Inicio() {
        return "Inicio";
    }


}