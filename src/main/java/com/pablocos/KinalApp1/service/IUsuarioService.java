package com.pablocos.KinalApp1.service;

import com.pablocos.KinalApp1.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    List<Usuario> listarTodos();

    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorEstado(Long estado);

    Usuario actualizar(Long Id, Usuario usuario);

    void eliminar(Long id);

    Optional<Usuario> findById(Long id);

    Optional<Usuario> buscarPorNombreUsuario(String nombreUsuario);

    boolean existePorID(Long id);

    List<Usuario> buscarPorEstadoConFor(Long estado);

    List<Usuario> listarUsuarios();
}
