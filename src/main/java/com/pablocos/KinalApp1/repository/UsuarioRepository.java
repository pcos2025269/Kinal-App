package com.pablocos.KinalApp1.repository;

import com.pablocos.KinalApp1.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    Optional<Usuario> findByNameUser(String nameUser);

    void deleteAllById(Long codigoUsuario);
    List<Usuario> findAll();

}
