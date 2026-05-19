package com.pablocos.KinalApp1.service;

import com.pablocos.KinalApp1.entity.Venta;

import java.util.List;
import java.util.Optional;

public interface IVentaService {
    List<Venta> listarVentas();
    Optional<Venta> buscarPorCodigoVenta(Long codigoVenta);
    Venta guardar(Venta venta);
    Venta actualizar(Long codigoVenta, Venta venta);
    void eliminar(Long codigoVenta);
}
