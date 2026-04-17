package com.pablocos.KinalApp1.service;

import com.pablocos.KinalApp1.entity.Cliente;
import com.pablocos.KinalApp1.entity.Usuario;
import com.pablocos.KinalApp1.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@Transactional
public class UserService implements IUsuarioService{

    @Autowired
    private final UsuarioRepository repository;

    public UserService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean validarAcceso(String nameUser, String passwordUser) {
        return repository.findByNameUser(nameUser)
                .map(u -> u.getPasswordUser().equals(passwordUser))
                .orElse(false);
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
    public Optional<Usuario> findByCodigoUsuario(Long codigoUsuario) {
        return Optional.empty();
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
        for (Usuario c :  usuarios) {
            if (c.getEstado() == estado) {
                filtrados.add(c);
            }
        }
        return filtrados;
    }

    private void validarUsuario(Usuario usuario){


        if (usuario.getNameUser() == null || usuario.getNameUser().trim().isEmpty()){
            throw new IllegalArgumentException("Se necesita un usernamer para continuar");
        }
        if (usuario.getPasswordUser() == null || usuario.getPasswordUser().trim().isEmpty()){
            throw new IllegalArgumentException("Se necesita que ingrese una contraseña");
        }

    }
}