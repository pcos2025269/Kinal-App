package com.pablocos.KinalApp1.service;

import com.pablocos.KinalApp1.entity.Usuario;
import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    List<Usuario> listarTodos();
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByCodigoUsuario(Long codigoUsuario);
    Usuario guardar(Usuario usuario);
    void eliminar(Long id);
    boolean existePorId(Long id);
    List<Usuario> buscarPorEstadoConFor(Long estado);

    boolean validarAcceso(String nameUser, String passwordUser);

    boolean existePorID(Long id);

    Usuario actualizar(Long id, Usuario usuario);
}