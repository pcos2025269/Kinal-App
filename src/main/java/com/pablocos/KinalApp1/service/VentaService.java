package com.pablocos.KinalApp1.service;

import com.pablocos.KinalApp1.entity.Cliente;
import com.pablocos.KinalApp1.entity.Venta;
import com.pablocos.KinalApp1.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VentaService implements IVentaService{

    private final VentaRepository ventaRepository;

    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> listarTodos() {
        return ventaRepository.findAll();
    }

    @Override
    public Venta guardar(Venta venta) {
        validarVenta(venta);
        if (venta.getCodigoVenta() == 0)
            venta.setCodigoVenta(1L);
        return ventaRepository.save(venta);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Venta> buscarPorCodigoVenta(Long codigoVenta) {
        return ventaRepository.findById(codigoVenta);
    }

    @Override
    public Venta actualizar(Long codigoVenta, Venta venta) {
        if(!ventaRepository.existsById(codigoVenta)){
            throw new RuntimeException("no se encuentra la venta con codigo: " +codigoVenta);
        }
        venta.setCodigoVenta(codigoVenta);
        validarVenta(venta);
        return ventaRepository.save(venta);
    }

    @Override
    public void eliminar(Long codigoVenta) {
        if(!ventaRepository.existsById(codigoVenta)){
            throw new RuntimeException("La venta no se encontro con el codigo: " + codigoVenta );
        }
        ventaRepository.deleteById(codigoVenta);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigoVenta(Long codigoVenta) {
        return ventaRepository.existsById(codigoVenta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> buscarPorEstadoConFor(Long estado) {
        List<Venta> ventas = ventaRepository.findAll();
        List<Venta> filtrados = new ArrayList<>();
        for (Venta c :  ventas) {
            if (c.getEstado() == estado) {
                filtrados.add(c);
            }
        }
        return filtrados;
    }

    private void validarVenta(Venta venta){
        if (venta.getUsuarioVenta() == null){
            throw new IllegalArgumentException("Se necesita un usuario para vender");
        }
        if (venta.getFechaVenta() == null){
            throw new IllegalArgumentException("Se necesita que ingrese la fecha");
        }
    }
}
