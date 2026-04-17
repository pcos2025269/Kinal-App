package com.pablocos.KinalApp1.repository;

import com.pablocos.KinalApp1.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente,String> {
    Optional<Cliente> findByDPICliente(String dpi);
}
