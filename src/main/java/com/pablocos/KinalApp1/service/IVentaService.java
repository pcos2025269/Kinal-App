package com.pablocos.KinalApp1.service;

import com.pablocos.KinalApp1.entity.Venta;

import java.util.List;
import java.util.Optional;

public interface IVentaService {
    List<Venta> listarTodos();

    Venta guardar(Venta venta);

    Optional<Venta> buscarPorCodigoVenta(Long codigoVenta);

    Venta actualizar (Long codigoVenta, Venta venta);

    void eliminar(Long codigoVenta);

    boolean existePorCodigoVenta (Long codigoVenta);

    List<Venta> buscarPorEstadoConFor(Long estado);
}
