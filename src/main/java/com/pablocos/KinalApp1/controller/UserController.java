package com.pablocos.KinalApp1.controller;

import com.pablocos.KinalApp1.entity.Usuario;
import com.pablocos.KinalApp1.service.IUsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final IUsuarioService usuarioService;

    public UserController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/lista")
    public String listarUsuarios(@RequestParam(name = "buscar", required = false) String buscar,
                                 Model model) {

        List<Usuario> usuarios;

        if (buscar != null && !buscar.trim().isEmpty()) {

            var usuarioOptional = usuarioService.buscarPorNombreUsuario(buscar);

            if (usuarioOptional.isPresent()) {
                usuarios = List.of(usuarioOptional.get());
                model.addAttribute("buscar", buscar);
            } else {
                usuarios = usuarioService.listarTodos().stream()
                        .filter(u ->
                                u.getNameUser().toLowerCase().contains(buscar.toLowerCase()) ||
                                        u.getEmail().toLowerCase().contains(buscar.toLowerCase()))
                        .toList();

                if (!usuarios.isEmpty()) {
                    model.addAttribute("buscar", buscar);
                } else {
                    model.addAttribute("error", "No se encontraron usuarios con: " + buscar);
                    usuarios = usuarioService.listarTodos();
                }
            }
        } else {
            usuarios = usuarioService.listarTodos();
        }

        model.addAttribute("usuarios", usuarios);
        return "users/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "users/formulario";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario, Model model) {
        try {
            usuarioService.guardar(usuario);
            return "redirect:/users/lista";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("usuario", usuario);
            return "users/formulario";
        }
    }

    @GetMapping("/ver/{id}")
    public String verDetalle(@PathVariable("id") Long id, Model model) {
        usuarioService.findById(id).ifPresentOrElse(
                u -> model.addAttribute("usuario", u),
                () -> model.addAttribute("error", "Usuario no encontrado")
        );
        return "users/detalle";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id, Model model) {
        usuarioService.findById(id).ifPresentOrElse(
                u -> model.addAttribute("usuario", u),
                () -> model.addAttribute("error", "Usuario no encontrado")
        );
        return "users/formulario";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarUsuario(@PathVariable("id") Long id,
                                    @ModelAttribute Usuario usuario,
                                    Model model) {
        try {
            usuario.setId(id);
            usuarioService.actualizar(id, usuario);
            return "redirect:/users/lista";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "users/formulario";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable("id") Long id) {
        usuarioService.eliminar(id);
        return "redirect:/users/lista";
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarId(@PathVariable("id") Long id) {
        return usuarioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = usuarioService.guardar(usuario);
            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        try {
            if (!usuarioService.existePorID(id)) {
                return ResponseEntity.notFound().build();
            }
            usuarioService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable("id") Long id,
                                        @RequestBody Usuario usuario) {
        try {
            if (!usuarioService.existePorID(id)) {
                return ResponseEntity.notFound().build();
            }
            Usuario usuarioActualizado = usuarioService.actualizar(id, usuario);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Usuario>> buscarPorEstado(@PathVariable("estado") Long estado) {
        List<Usuario> usuario = usuarioService.buscarPorEstadoConFor(estado);

        if (usuario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/buscarEstado")
    public String buscarPorEstadoVista(@RequestParam(name = "estado", required = false) Long estado,
                                       Model model) {

        if (estado != null) {

            List<Usuario> usuarios = usuarioService.buscarPorEstadoConFor(estado);

            model.addAttribute("usuarios", usuarios);
            model.addAttribute("estado", estado);

            if (usuarios.isEmpty()) {
                model.addAttribute("error", "No se encontraron usuarios con estado: " + estado);
            }

        } else {
            model.addAttribute("usuarios", usuarioService.listarTodos());
        }

        return "users/lista";
    }
}