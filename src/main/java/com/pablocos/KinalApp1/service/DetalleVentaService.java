package com.pablocos.KinalApp1.service;

import com.pablocos.KinalApp1.entity.DetalleVenta;
import com.pablocos.KinalApp1.repository.DetalleVentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DetalleVentaService implements IDetalleVentaSerrvice {

    private final DetalleVentaRepository detalleVentaRepository;

    public DetalleVentaService(DetalleVentaRepository detalleVentaRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> listarTodos() {
        return detalleVentaRepository.findAll();
    }

    @Override
    public DetalleVenta guardar(DetalleVenta detalleVenta) {
        validarDetalleVenta(detalleVenta);

        return detalleVentaRepository.save(detalleVenta);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DetalleVenta> buscarPotCodigoUsuario(Long codigoDetalleVenta) {
        return detalleVentaRepository.findById(codigoDetalleVenta);
    }

    @Override
    public DetalleVenta actualizar(Long codigoDetalleVenta, DetalleVenta detalleVenta) {
        if(!detalleVentaRepository.existsById(codigoDetalleVenta)){
            throw new RuntimeException("No se encontro el detalle de venta con el codigo:  " + codigoDetalleVenta);
            //Si no existe se lanza una excepcion y a esto se le denomina(error controlado)
        }
        detalleVenta.setCodigoDetalleVenta(codigoDetalleVenta);
        return detalleVentaRepository.save(detalleVenta);
    }

    @Override
    public void eliminar(Long codigoUsuario) {
        if(!detalleVentaRepository.existsById(codigoUsuario)){
            throw new RuntimeException("No se encontro el detalle de venta con el codigo: " + codigoUsuario );
        }
        detalleVentaRepository.deleteById(codigoUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(Long codigoDetalleVenta) {
        return detalleVentaRepository.existsById(codigoDetalleVenta);
    }

    private void  validarDetalleVenta(DetalleVenta detalleVenta){
        if(detalleVenta.getCantidad() == null){
            throw new IllegalArgumentException("Se necesita que agregue una cantidad");
        }

        if(detalleVenta.getPrecioUnitario() == null){
            throw new IllegalArgumentException("Se necesita que agrege un precio unitario");
        }
    }
}
