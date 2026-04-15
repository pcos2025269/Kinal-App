package com.pablocos.KinalApp1.service;

import com.pablocos.KinalApp1.entity.Usuario;
import com.pablocos.KinalApp1.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UserService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> findByCodigoUsuario(Long codigoUsuario) {
        return usuarioRepository.findById(codigoUsuario);
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public boolean existePorId(Long id) {
        return usuarioRepository.existsById(id);
    }

    @Override
    public List<Usuario> buscarPorEstadoConFor(Long estado) {
        return usuarioRepository.findByEstado(estado);
    }

    @Override
    public boolean validarAcceso(String nameUser, String passwordUser) {
        return usuarioRepository.findByNameUserAndPasswordUser(nameUser, passwordUser).isPresent();
    }

    @Override
    public boolean existePorID(Long id) {
        return usuarioRepository.existsById(id);
    }

    @Override
    public Usuario actualizar(Long id, Usuario usuario) {
        if (existePorId(id)) {
            usuario.setId(id);
            return usuarioRepository.save(usuario);
        }
        throw new RuntimeException("Usuario no encontrado con id: " + id);
    }
}