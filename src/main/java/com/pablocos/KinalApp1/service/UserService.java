package com.pablocos.KinalApp1.service;

import com.pablocos.KinalApp1.entity.Usuario;
import com.pablocos.KinalApp1.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements IUsuarioService {

    private final UsuarioRepository repository;

    public UserService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        validarUsuario(usuario);
        if (usuario.getEstado() == 0)
            usuario.setEstado(1L);
        return repository.save(usuario);
    }

    @Override
    public Optional<Usuario> buscarPorEstado(Long estado) {
        return Optional.empty();
    }

    @Override
    public Usuario actualizar(Long codigoUsuario, Usuario usuario) {
        if (!repository.existsById(codigoUsuario)){
            throw new IllegalArgumentException("No se encontro Usuario con el codigo:" + codigoUsuario);
        }
        usuario.setId(codigoUsuario);
        validarUsuario(usuario);
        return repository.save(usuario);
    }

    @Override
    public void eliminar(Long codigoUsuario) {
        if (!repository.existsById(codigoUsuario)){
            throw new IllegalArgumentException("No se encontro Usuario con el codigo:" + codigoUsuario);
        }
        repository.deleteById(codigoUsuario);
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorID(Long id) {
        return repository.existsById(id);
    }

    @Override
    public List<Usuario> buscarPorEstadoConFor(Long estado) {
        List<Usuario> usuarios = repository.findAll();
        List<Usuario> filtrados = new ArrayList<>();
        for (Usuario c : usuarios) {
            if (c.getEstado() == estado) {
                filtrados.add(c);
            }
        }
        return filtrados;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {
        return repository.findAll();
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario.getNameUser() == null || usuario.getNameUser().trim().isEmpty()){
            throw new IllegalArgumentException("Se necesita un usernamer para continuar");
        }
        if (usuario.getPasswordUser() == null || usuario.getPasswordUser().trim().isEmpty()){
            throw new IllegalArgumentException("Se necesita que ingrese una contraseña");
        }
    }
}
