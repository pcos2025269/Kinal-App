package com.pablocos.KinalApp1.controller;

import com.pablocos.KinalApp1.entity.DetalleVenta;
import com.pablocos.KinalApp1.entity.Producto;
import com.pablocos.KinalApp1.entity.Venta;
import com.pablocos.KinalApp1.service.IDetalleVentaSerrvice;
import com.pablocos.KinalApp1.service.IProductosService;
import com.pablocos.KinalApp1.service.IVentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/detalleVenta")
public class DetalleVentaController {

    private final IDetalleVentaSerrvice detalleVentaService;
    private final IProductosService productosService;
    private final IVentaService ventaService;

    public DetalleVentaController(IDetalleVentaSerrvice detalleVentaService,
                                  IProductosService productosService,
                                  IVentaService ventaService) {
        this.detalleVentaService = detalleVentaService;
        this.productosService = productosService;
        this.ventaService = ventaService;
    }

    @GetMapping("/lista")
    public String listarVista(@RequestParam(required = false) String buscar, Model model) {
        List<DetalleVenta> detalles;

        if (buscar != null && !buscar.trim().isEmpty()) {
            try {
                Long codigo = Long.parseLong(buscar);
                var detalleOptional = detalleVentaService.buscarPotCodigoUsuario(codigo);

                if (detalleOptional.isPresent()) {
                    detalles = List.of(detalleOptional.get());
                    model.addAttribute("buscar", buscar);
                } else {
                    detalles = detalleVentaService.listarTodos().stream()
                            .filter(d -> d.getDetalleproducto().getNombreProducto().toLowerCase().contains(buscar.toLowerCase()))
                            .toList();

                    if (!detalles.isEmpty()) {
                        model.addAttribute("buscar", buscar);
                    } else {
                        model.addAttribute("error", "No se encontraron detalles con: " + buscar);
                        detalles = detalleVentaService.listarTodos();
                    }
                }
            } catch (NumberFormatException e) {
                detalles = detalleVentaService.listarTodos().stream()
                        .filter(d -> d.getDetalleproducto().getNombreProducto().toLowerCase().contains(buscar.toLowerCase()))
                        .toList();

                if (!detalles.isEmpty()) {
                    model.addAttribute("buscar", buscar);
                } else {
                    model.addAttribute("error", "No se encontraron detalles con: " + buscar);
                    detalles = detalleVentaService.listarTodos();
                }
            }
        } else {
            detalles = detalleVentaService.listarTodos();
        }

        model.addAttribute("detalles", detalles);
        return "DetalleVenta/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("detalleVenta", new DetalleVenta());
        model.addAttribute("productos", productosService.listarTodos());
        model.addAttribute("ventas", ventaService.listarTodos());
        return "DetalleVenta/formulario";
    }

    @PostMapping("/guardar")
    public String guardarDetalleVenta(@RequestParam Long codigoProducto,
                                      @RequestParam Long codigoVenta,
                                      @RequestParam Long cantidad,
                                      Model model) {
        try {
            Producto producto = productosService.buscarPorId(codigoProducto).orElse(null);
            Venta venta = ventaService.buscarPorCodigoVenta(codigoVenta).orElse(null);

            if (producto == null) {
                model.addAttribute("error", "Producto no encontrado");
                model.addAttribute("productos", productosService.listarTodos());
                model.addAttribute("ventas", ventaService.listarTodos());
                return "DetalleVenta/formulario";
            }

            if (venta == null) {
                model.addAttribute("error", "Venta no encontrada");
                model.addAttribute("productos", productosService.listarTodos());
                model.addAttribute("ventas", ventaService.listarTodos());
                return "DetalleVenta/formulario";
            }

            BigDecimal precioUnitario = producto.getPrecio();
            BigDecimal subTotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));

            DetalleVenta detalleVenta = new DetalleVenta();
            detalleVenta.setDetalleproducto(producto);
            detalleVenta.setDetalleVenta(venta);
            detalleVenta.setCantidad(cantidad);
            detalleVenta.setPrecioUnitario(precioUnitario);
            detalleVenta.setSubTotal(subTotal);

            detalleVentaService.guardar(detalleVenta);
            return "redirect:/DetalleVenta/lista";
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar: " + e.getMessage());
            model.addAttribute("productos", productosService.listarTodos());
            model.addAttribute("ventas", ventaService.listarTodos());
            return "DetalleVenta/formulario";
        }
    }

    @GetMapping("/ver/{codigoDetalleVenta}")
    public String verDetalle(@PathVariable Long codigoDetalleVenta, Model model) {
        detalleVentaService.buscarPotCodigoUsuario(codigoDetalleVenta)
                .ifPresentOrElse(
                        detalle -> model.addAttribute("detalle", detalle),
                        () -> model.addAttribute("error", "Detalle no encontrado")
                );
        return "DetalleVenta/detalle";
    }

    @GetMapping("/editar/{codigoDetalleVenta}")
    public String mostrarFormularioEditar(@PathVariable Long codigoDetalleVenta, Model model) {
        detalleVentaService.buscarPotCodigoUsuario(codigoDetalleVenta)
                .ifPresentOrElse(
                        detalle -> {
                            model.addAttribute("detalleVenta", detalle);
                            model.addAttribute("productos", productosService.listarTodos());
                            model.addAttribute("ventas", ventaService.listarTodos());
                        },
                        () -> model.addAttribute("error", "Detalle no encontrado")
                );
        return "DetalleVenta/formulario";
    }

    @PostMapping("/actualizar/{codigoDetalleVenta}")
    public String actualizarDetalleVenta(@PathVariable Long codigoDetalleVenta,
                                         @RequestParam Long codigoProducto,
                                         @RequestParam Long codigoVenta,
                                         @RequestParam Long cantidad,
                                         Model model) {
        try {
            Producto producto = productosService.buscarPorId(codigoProducto).orElse(null);
            Venta venta = ventaService.buscarPorCodigoVenta(codigoVenta).orElse(null);

            if (producto == null || venta == null) {
                model.addAttribute("error", "Producto o Venta no encontrado");
                return "DetalleVenta/formulario";
            }

            BigDecimal precioUnitario = producto.getPrecio();
            BigDecimal subTotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));

            DetalleVenta detalleVenta = new DetalleVenta();
            detalleVenta.setCodigoDetalleVenta(codigoDetalleVenta);
            detalleVenta.setDetalleproducto(producto);
            detalleVenta.setDetalleVenta(venta);
            detalleVenta.setCantidad(cantidad);
            detalleVenta.setPrecioUnitario(precioUnitario);
            detalleVenta.setSubTotal(subTotal);

            detalleVentaService.actualizar(codigoDetalleVenta, detalleVenta);
            return "redirect:/DetalleVenta/lista";
        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar: " + e.getMessage());
            return "DetalleVenta/formulario";
        }
    }

    @GetMapping("/eliminar/{codigoDetalleVenta}")
    public String eliminarDetalleVenta(@PathVariable Long codigoDetalleVenta) {
        if (detalleVentaService.existePorCodigo(codigoDetalleVenta)) {
            detalleVentaService.eliminar(codigoDetalleVenta);
        }
        return "redirect:/DetalleVenta/lista";
    }

    @GetMapping("/api")
    public ResponseEntity<List<DetalleVenta>> listarApi() {
        return ResponseEntity.ok(detalleVentaService.listarTodos());
    }

    @GetMapping("/api/{codigoDetalleVenta}")
    public ResponseEntity<DetalleVenta> buscarApi(@PathVariable Long codigoDetalleVenta) {
        return detalleVentaService.buscarPotCodigoUsuario(codigoDetalleVenta)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api")
    public ResponseEntity<?> guardarApi(@RequestBody DetalleVenta detalleVenta) {
        try {
            return new ResponseEntity<>(detalleVentaService.guardar(detalleVenta), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/api/{codigoDetalleVenta}")
    public ResponseEntity<Void> eliminarApi(@PathVariable Long codigoDetalleVenta) {
        if (!detalleVentaService.existePorCodigo(codigoDetalleVenta)) {
            return ResponseEntity.notFound().build();
        }
        detalleVentaService.eliminar(codigoDetalleVenta);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/api/{codigoDetalleVenta}")
    public ResponseEntity<?> actualizarApi(@PathVariable Long codigoDetalleVenta, @RequestBody DetalleVenta detalleVenta) {
        try {
            return ResponseEntity.ok(detalleVentaService.actualizar(codigoDetalleVenta, detalleVenta));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}