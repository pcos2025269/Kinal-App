package com.pablocos.KinalApp1.service;

import com.pablocos.KinalApp1.entity.DetalleVenta;
import java.util.List;
import java.util.Optional;

public interface IDetalleVentaService {
    List<DetalleVenta> listarTodos();
    DetalleVenta guardar(DetalleVenta detalleVenta);
    Optional<DetalleVenta> buscarPorId(Long codigoDetalleVenta);
}