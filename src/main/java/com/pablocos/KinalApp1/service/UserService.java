package com.pablocos.KinalApp1.service;

import com.pablocos.KinalApp1.entity.Usuario;
import com.pablocos.KinalApp1.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@Transactional
public class UserService implements IUsuarioService{

    private final UsuarioRepository usuarioRepository;

    public UserService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        validarUsuario(usuario);
        if (usuario.getEstado() == 0)
            usuario.setEstado(1L);
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> buscarPorEstado(Long estado) {
        return Optional.empty();
    }

    @Override
    public Usuario actualizar(Long codigoUsuario, Usuario usuario) {
        if (!usuarioRepository.existsById(codigoUsuario)){
            throw new IllegalArgumentException("No se encontro Usuario con el codigo:" + codigoUsuario);
        }
        usuario.setCodigoUsuario(codigoUsuario);
        validarUsuario(usuario);
        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminar(Long codigoUsuario) {
        if (!usuarioRepository.existsById(codigoUsuario)){
            throw new IllegalArgumentException("No se encontro Usuario con el codigo:" + codigoUsuario);
        }
        usuarioRepository.deleteAllById(codigoUsuario);
    }

    private void validarUsuario(Usuario usuario){
         if (usuario.getCodigoUsuario() == null){
             throw new IllegalArgumentException("Se necesita que ingrese el codigo del usuario");
         }

         if (usuario.getNameUser() == null || usuario.getNameUser().trim().isEmpty()){
             throw new IllegalArgumentException("Se necesita un usernamer para continuar");
         }
         if (usuario.getPasswordUser() == null || usuario.getPasswordUser().trim().isEmpty()){
             throw new IllegalArgumentException("Se necesita que ingrese una contraseña");
         }

    }
}
