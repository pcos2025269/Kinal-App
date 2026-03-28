package com.pablocos.KinalApp1.service;

import com.pablocos.KinalApp1.entity.Cliente;
import com.pablocos.KinalApp1.entity.Producto;
import com.pablocos.KinalApp1.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService implements IProductosService {
    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto guardar(Producto producto) {

        validarProducto(producto);
        if (producto.getEstado() == 0)
            producto.setEstado(1L);
        return productoRepository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorId(Long codigoProducto) {
        return productoRepository.findById(codigoProducto);
    }

    @Override
    public Producto actualizar(Long codigoProducto, Producto producto) {
        if(!productoRepository.existsById(codigoProducto)){
            throw new RuntimeException("El producto no se encontro con el codigo: " + codigoProducto);
        }
        producto.setCodigoProducto(codigoProducto);
        validarProducto(producto);
        return productoRepository.save(producto);
    }

    @Override
    public void eliminar(Long codigoProducto) {
        if (!productoRepository.existsById(codigoProducto)){
            throw new RuntimeException("El producto no se encontro con el codigo: " + codigoProducto);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorcodigo(Long codigoProducto) {
        return productoRepository.existsById(codigoProducto);
    }

    @Override
    public List<Producto> buscarrPorEstadoConFor(Long Estado) {
        List<Producto> productos = productoRepository.findAll();
        List<Producto> filtrados = new ArrayList<>();
        for (Producto c :  productos) {
            if (c.getEstado() == Estado) {
                filtrados.add(c);
            }
        }
        return filtrados;
    }


    private void validarProducto(Producto producto){
        if (producto.getNombreProducto() == null || producto.getNombreProducto().trim().isEmpty()){
            throw new IllegalArgumentException("El Nombre del producto es obligatorio");
        }
        if (producto.getEstado() == null){
            throw new IllegalArgumentException("Se necesita que llene el estado del producto");
        }
    }
}
