package com.pablocos.KinalApp1.service;

import com.pablocos.KinalApp1.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    List<Usuario> listarTodos();

    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorEstado(Long estado);

    Usuario actualizar(Long codigoUsuario, Usuario usuario);

    void eliminar(Long codigoUsuario);

    Optional<Usuario> findByCodigoUsuario(Long codigoUsuario);
    boolean existePorID(Long id);

    List<Usuario> buscarPorEstadoConFor (Long estado);

}
