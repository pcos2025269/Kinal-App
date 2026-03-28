package com.pablocos.KinalApp1.controller;


import com.pablocos.KinalApp1.entity.Cliente;
import com.pablocos.KinalApp1.entity.DetalleVenta;
import com.pablocos.KinalApp1.service.IDetalleVentaSerrvice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detalleVenta")
public class DetalleVentaController {
    private final IDetalleVentaSerrvice detalleVentaSerrvice;

    public DetalleVentaController(IDetalleVentaSerrvice detalleVentaSerrvice) {
        this.detalleVentaSerrvice = detalleVentaSerrvice;
    }
    @GetMapping
    public ResponseEntity<List<DetalleVenta>> listar(){
        List<DetalleVenta> detalleVenta = detalleVentaSerrvice.listarTodos();
        return ResponseEntity.ok(detalleVenta);
    }

    @GetMapping("/{codigoDetalleVenta}")
    public ResponseEntity<DetalleVenta> buscarPorCode(@PathVariable Long codigoDetalleVenta){
        return detalleVentaSerrvice.buscarPotCodigoUsuario(codigoDetalleVenta)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody DetalleVenta detalleVenta){
        try{
            DetalleVenta nuevodetalle = detalleVentaSerrvice.guardar(detalleVenta);
            return new ResponseEntity<>(nuevodetalle, HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{codigoDetalleVenta}")
    public ResponseEntity<Void> eliminar (@PathVariable Long codigoDetalleVenta){
        //ResponseEntity<void>: No devuelve cuerpo en la respuesta
        try {
            if (!detalleVentaSerrvice.existePorCodigo(codigoDetalleVenta)){
                return ResponseEntity.notFound().build();
            }
            detalleVentaSerrvice.eliminar(codigoDetalleVenta);
            return ResponseEntity.noContent().build();
            //204 NO CONTENT (Se ejecuto correctamente y no devuelve cuerpo)
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
            // 404 NOT FOUND
        }

    }

    @PutMapping("/{codigoDetalleVenta}")
    public ResponseEntity<?> actualizar(@PathVariable Long codigoDetalleVenta,@RequestBody DetalleVenta detalleVenta){
        try {
            if (!detalleVentaSerrvice.existePorCodigo(codigoDetalleVenta)){

                return ResponseEntity.notFound().build();
            }
            //Actualizamos el cliente pero puede lanzar una exception
            DetalleVenta detalleActualizado = detalleVentaSerrvice.actualizar(codigoDetalleVenta,detalleVenta);
            return ResponseEntity.ok(detalleActualizado);
            //200 ok con el cliente ya actualizado
        }catch (IllegalArgumentException e){
            //Error cuando los datos sean incorrectos
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            //Posiblemente cualquier otro error como cliente no encontrado, etc
            //404 NOT FOUND
            return ResponseEntity.notFound().build();
        }
    }

}
