package com.pablocos.KinalApp1.controller;

import com.pablocos.KinalApp1.entity.Producto;
import com.pablocos.KinalApp1.service.IProductosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoController {
    private final IProductosService productosService;

    public ProductoController(IProductosService productosService) {
        this.productosService = productosService;
    }

    @GetMapping("/lista")
    public String listarVista(@RequestParam(value = "buscar", required = false) String buscar, Model model) {
        List<Producto> productos;

        if (buscar != null && !buscar.trim().isEmpty()) {
            try {
                Long codigo = Long.parseLong(buscar);
                var productoOptional = productosService.buscarPorId(codigo);

                if (productoOptional.isPresent()) {
                    productos = List.of(productoOptional.get());
                    model.addAttribute("buscar", buscar);
                } else {
                    productos = productosService.listarTodos().stream()
                            .filter(p -> p.getNombreProducto().toLowerCase().contains(buscar.toLowerCase()))
                            .toList();

                    if (!productos.isEmpty()) {
                        model.addAttribute("buscar", buscar);
                    } else {
                        model.addAttribute("error", "No se encontraron productos con: " + buscar);
                        productos = productosService.listarTodos();
                    }
                }
            } catch (NumberFormatException e) {
                productos = productosService.listarTodos().stream()
                        .filter(p -> p.getNombreProducto().toLowerCase().contains(buscar.toLowerCase()))
                        .toList();

                if (!productos.isEmpty()) {
                    model.addAttribute("buscar", buscar);
                } else {
                    model.addAttribute("error", "No se encontraron productos con: " + buscar);
                    productos = productosService.listarTodos();
                }
            }
        } else {
            productos = productosService.listarTodos();
        }

        model.addAttribute("productos", productos);
        return "productos/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("producto", new Producto());
        return "productos/formulario";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto, Model model) {
        try {
            productosService.guardar(producto);
            return "redirect:/productos/lista";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("producto", producto);
            return "productos/formulario";
        }
    }

    @GetMapping("/ver/{codigoProducto}")
    public String verDetalle(@PathVariable("codigoProducto") Long codigoProducto,
                             Model model) {
        productosService.buscarPorId(codigoProducto)
                .ifPresentOrElse(
                        producto -> model.addAttribute("producto", producto),
                        () -> model.addAttribute("error", "Producto no encontrado")
                );
        return "productos/detalle";
    }

    @GetMapping("/editar/{codigoProducto}")
    public String mostrarFormularioEditar( @PathVariable("codigoProducto") Long codigoProducto,
                                           Model model) {
        productosService.buscarPorId(codigoProducto)
                .ifPresentOrElse(
                        producto -> model.addAttribute("producto", producto),
                        () -> model.addAttribute("error", "Producto no encontrado")
                );
        return "productos/formulario";
    }

    @PostMapping("/actualizar/{codigoProducto}")
    public String actualizarProducto(@PathVariable("codigoProducto") Long codigoProducto,
                                     @ModelAttribute Producto producto,
                                     Model model) {
        try {
            producto.setCodigoProducto(codigoProducto);
            productosService.actualizar(codigoProducto, producto);
            return "redirect:/productos/lista";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Producto no encontrado");
            return "productos/formulario";
        }
    }

    @GetMapping("/eliminar/{codigoProducto}")
    public String eliminarProducto(@PathVariable("codigoProducto") Long codigoProducto
    ) {
        if (productosService.existePorcodigo(codigoProducto)) {
            productosService.eliminar(codigoProducto);
        }
        return "redirect:/productos/lista";
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        List<Producto> productos = productosService.listarTodos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/api/{codigoProducto}")
    public ResponseEntity<Producto> buscarPorCodigo(@PathVariable("codigoProducto") Long codigoProducto
    ) {
        return productosService.buscarPorId(codigoProducto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Producto producto) {
        try {
            Producto nuevoProducto = productosService.guardar(producto);
            return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{codigoProducto}")
    public ResponseEntity<Void> eliminar(@PathVariable("codigoProducto") Long codigoProducto) {
        try {
            if (!productosService.existePorcodigo(codigoProducto)) {
                return ResponseEntity.notFound().build();
            }
            productosService.eliminar(codigoProducto);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{codigoProducto}")
    public ResponseEntity<?> actualizar(@PathVariable("codigoProducto") Long codigoProducto, @RequestBody Producto producto) {
        try {
            if (!productosService.existePorcodigo(codigoProducto)) {
                return ResponseEntity.notFound().build();
            }
            Producto productoActualizado = productosService.actualizar(codigoProducto, producto);
            return ResponseEntity.ok(productoActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Producto>> buscarPorEstado(@PathVariable("estado") Long estado) {
        List<Producto> productos = productosService.buscarrPorEstadoConFor(estado);
        if (productos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productos);
    }
}