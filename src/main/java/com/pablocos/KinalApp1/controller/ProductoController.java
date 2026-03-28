package com.pablocos.KinalApp1.controller;

import com.pablocos.KinalApp1.entity.Cliente;
import com.pablocos.KinalApp1.entity.Producto;

import com.pablocos.KinalApp1.service.IProductosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {
    private final IProductosService productosService;

    public ProductoController(IProductosService productosService) {
        this.productosService = productosService;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listar(){
        List<Producto> productos = productosService.listarTodos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{codigoProducto}")
    public ResponseEntity<Producto> buscarPorCodigo(@PathVariable Long codigoProducto){
        return  productosService.buscarPorId(codigoProducto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> guardar (@RequestBody Producto producto){
        try{
            Producto nuevoProducto = productosService.guardar(producto);
            return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{codigoProducto}")
    public ResponseEntity<Void> eliminar (@PathVariable Long codigoProducto){
        try{
            if(!productosService.existePorcodigo(codigoProducto)){
                return ResponseEntity.notFound().build();
            }
            productosService.eliminar(codigoProducto);
            return ResponseEntity.noContent().build();
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }

    }

    @PutMapping("/{codigoProducto}")
    public ResponseEntity<?> actualizar(@PathVariable Long codigoProducto, @RequestBody Producto producto){
        try{
            if(!productosService.existePorcodigo(codigoProducto)){
                return ResponseEntity.notFound().build();
            }

            Producto ProductoActualizado = productosService.actualizar(codigoProducto,producto);
            return ResponseEntity.ok(ProductoActualizado);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Producto>> buscarPorEstado(@PathVariable Long estado) {
        List<Producto>  productos = productosService.buscarrPorEstadoConFor(estado);
        if (productos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productos);
    }
}
