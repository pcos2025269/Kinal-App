package com.pablocos.KinalApp1.service;


import com.pablocos.KinalApp1.entity.Producto;

import java.util.List;
import java.util.Optional;

public interface IProductosService {
    List<Producto> listarTodos();

    Producto guardar(Producto producto);

    Optional<Producto> buscarPorId(Long codigoProducto);

    Producto actualizar(Long codigoProducto,Producto producto);

    void eliminar(Long codigoProducto);

    boolean existePorcodigo(Long codigoProducto);

    List<Producto> buscarrPorEstadoConFor(Long Estado);
}
