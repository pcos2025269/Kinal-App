package com.pablocos.KinalApp1.controller;

import com.pablocos.KinalApp1.entity.DetalleVenta;
import com.pablocos.KinalApp1.entity.Producto;
import com.pablocos.KinalApp1.entity.Venta;
import com.pablocos.KinalApp1.service.IDetalleVentaService;
import com.pablocos.KinalApp1.service.IProductosService;
import com.pablocos.KinalApp1.service.IVentaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/detalleVenta")
public class DetalleVentaController {

    private final IDetalleVentaService detalleVentaService;
    private final IProductosService productosService;
    private final IVentaService ventaService;

    public DetalleVentaController(IDetalleVentaService detalleVentaService,
                                  IProductosService productosService,
                                  IVentaService ventaService) {
        this.detalleVentaService = detalleVentaService;
        this.productosService = productosService;
        this.ventaService = ventaService;
    }

    @GetMapping("/lista")
    public String listarVista(@RequestParam(name = "buscar", required = false) String buscar, Model model) {
        List<DetalleVenta> detalles = detalleVentaService.listarTodos();
        model.addAttribute("detalles", detalles);
        return "detalleVenta/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("detalleVenta", new DetalleVenta());
        model.addAttribute("productos", productosService.listarTodos());
        model.addAttribute("ventas", ventaService.listarVentas());
        return "detalleVenta/formulario";
    }

    @PostMapping("/guardar")
    public String guardarDetalleVenta(@RequestParam(name = "codigoProducto") Long codigoProducto,
                                      @RequestParam(name = "codigoVenta") Long codigoVenta,
                                      @RequestParam(name = "cantidad") Long cantidad,
                                      Model model) {
        try {
            Producto producto = productosService.buscarPorId(codigoProducto).orElse(null);
            Venta venta = ventaService.buscarPorCodigoVenta(codigoVenta).orElse(null);
            if (producto == null || venta == null) {
                model.addAttribute("error", "Producto o Venta no válidos");
                cargarDatosFormulario(model);
                return "detalleVenta/formulario";
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
            return "redirect:/detalleVenta/lista";
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar: " + e.getMessage());
            cargarDatosFormulario(model);
            return "detalleVenta/formulario";
        }
    }

    @GetMapping("/ver/{codigoDetalleVenta}")
    public String verDetalle(@PathVariable(name = "codigoDetalleVenta") Long codigoDetalleVenta, Model model) {
        DetalleVenta detalle = detalleVentaService.buscarPorId(codigoDetalleVenta).orElse(null);
        model.addAttribute("detalle", detalle);
        if (detalle == null) model.addAttribute("error", "Detalle no encontrado");
        return "detalleVenta/detalle";
    }

    private void cargarDatosFormulario(Model model) {
        model.addAttribute("productos", productosService.listarTodos());
        model.addAttribute("ventas", ventaService.listarVentas());
    }
}