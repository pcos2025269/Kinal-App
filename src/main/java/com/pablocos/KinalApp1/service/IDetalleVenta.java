package com.pablocos.KinalApp1.service;

import com.pablocos.KinalApp1.entity.DetalleVenta;


import java.util.List;
import java.util.Optional;

public interface IDetalleVenta {
    List<DetalleVenta> listarTodos();

    DetalleVenta guardar(DetalleVenta detalleVenta);

    Optional<DetalleVenta> buscarPotCodigoUsuario(Long codigoDetalleVenta);

    DetalleVenta actualizar(Long codigoDetalleVenta, DetalleVenta detalleVenta);

    void eliminar(Long codigoUsuario);
    
    boolean existePorCodigo(Long codigoDetalleVenta);

    List<DetalleVenta> buscarPorEstadoConFor (Long estado);
}
