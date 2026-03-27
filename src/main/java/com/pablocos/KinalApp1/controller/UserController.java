package com.pablocos.KinalApp1.controller;

import com.pablocos.KinalApp1.entity.Usuario;
import com.pablocos.KinalApp1.service.IUsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final IUsuarioService usuarioService;

    public UserController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity <List<Usuario>> listar(){
        List<Usuario> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarId(@PathVariable Long id){
        return usuarioService.findByCodigoUsuario(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Usuario usuario){
        try {
            Usuario nuevoUsuario = usuarioService.guardar(usuario);
            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar (@PathVariable Long id){
        try{
            if (!usuarioService.existePorID(id)){
                return ResponseEntity.notFound().build();
            }
            usuarioService.eliminar(id);
            return ResponseEntity.noContent().build();
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar (@PathVariable Long id,@RequestBody Usuario usuario){
        try{
            if (!usuarioService.existePorID(id)){
                return ResponseEntity.notFound().build();
            }
            Usuario usuarioActualizado = usuarioService.actualizar(id,usuario);
            return ResponseEntity.ok(usuarioActualizado);
        }catch(IllegalArgumentException e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

}
