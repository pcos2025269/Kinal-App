package com.pablocos.KinalApp1.service;

import com.pablocos.KinalApp1.entity.Venta;
import com.pablocos.KinalApp1.repository.VentaRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class VentaService implements IVentaService {
    private final VentaRepository ventaRepository;

    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    @Override
    public Optional<Venta> buscarPorCodigoVenta(Long codigoVenta) {
        return ventaRepository.findById(codigoVenta);
    }

    @Override
    public Venta guardar(Venta venta) {
        // El ID se genera automáticamente, no lo asignamos
        return ventaRepository.save(venta);
    }

    @Override
    public Venta actualizar(Long codigoVenta, Venta venta) {
        if (!ventaRepository.existsById(codigoVenta)) {
            throw new RuntimeException("Venta no encontrada");
        }
        venta.setCodigoVenta(codigoVenta);
        return ventaRepository.save(venta);
    }

    @Override
    public void eliminar(Long codigoVenta) {
        ventaRepository.deleteById(codigoVenta);
    }

    @Override
    public @Nullable Object listarTodos() {
        return null;
    }
}