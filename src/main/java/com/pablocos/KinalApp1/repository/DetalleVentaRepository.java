package com.pablocos.KinalApp1.repository;

import com.pablocos.KinalApp1.entity.Cliente;
import com.pablocos.KinalApp1.entity.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta,Long>{
    List<DetalleVenta> findAll();
}
