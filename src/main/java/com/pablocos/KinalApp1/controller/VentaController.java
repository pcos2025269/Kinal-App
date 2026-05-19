package com.pablocos.KinalApp1.controller;

import com.pablocos.KinalApp1.entity.*;
import com.pablocos.KinalApp1.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/venta")
public class VentaController {

    private final IVentaService ventaService;
    private final IClienteService clienteService;
    private final IUsuarioService usuarioService;
    private final IProductosService productosService;
    private final IDetalleVentaService detalleVentaService;

    public VentaController(IVentaService ventaService,
                           IClienteService clienteService,
                           IUsuarioService usuarioService,
                           IProductosService productosService,
                           IDetalleVentaService detalleVentaService) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.usuarioService = usuarioService;
        this.productosService = productosService;
        this.detalleVentaService = detalleVentaService;
    }

    // Listar todas las ventas
    @GetMapping("/lista")
    public String listarVista(@RequestParam(name = "buscar", required = false) String buscar, Model model) {
        List<Venta> ventas = ventaService.listarVentas();
        model.addAttribute("ventas", ventas);
        return "venta/lista";
    }

    // Mostrar formulario para nueva venta
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("venta", new Venta());
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "venta/formulario";
    }

    // Guardar nueva venta (con fecha y estado)
    @PostMapping("/guardar")
    public String guardarVenta(@RequestParam(name = "dpiCliente") String dpiCliente,
                               @RequestParam(name = "idUsuario") Long idUsuario,
                               @RequestParam(name = "fechaVenta") String fechaVenta,
                               @RequestParam(name = "estado") Long estado,
                               @RequestParam(name = "codigoProducto", required = false) List<Long> codigosProducto,
                               @RequestParam(name = "cantidad", required = false) List<Long> cantidades,
                               Model model) {
        try {
            // Validar cliente y usuario
            Cliente cliente = clienteService.buscarPorDPI(dpiCliente.trim()).orElse(null);
            Usuario usuario = (idUsuario != null) ? usuarioService.findById(idUsuario).orElse(null) : null;

            if (cliente == null) {
                model.addAttribute("error", "Cliente no válido (DPI: " + dpiCliente + ")");
                cargarDatosFormulario(model);
                return "venta/formulario";
            }
            if (usuario == null) {
                model.addAttribute("error", "Usuario no válido (ID: " + idUsuario + ")");
                cargarDatosFormulario(model);
                return "venta/formulario";
            }

            // Validar que venga al menos un producto
            if (codigosProducto == null || codigosProducto.isEmpty()) {
                model.addAttribute("error", "Debe agregar al menos un producto a la venta.");
                cargarDatosFormulario(model);
                return "venta/formulario";
            }

            // Construir detalles y calcular total
            List<DetalleVenta> detalles = new ArrayList<>();
            BigDecimal total = BigDecimal.ZERO;

            for (int i = 0; i < codigosProducto.size(); i++) {
                Long codProd = codigosProducto.get(i);
                Long cantidad = (cantidades != null && i < cantidades.size()) ? cantidades.get(i) : 1L;

                if (codProd == null || cantidad == null || cantidad <= 0) continue;

                Producto producto = productosService.buscarPorId(codProd).orElse(null);
                if (producto == null) continue;

                BigDecimal precioUnitario = producto.getPrecio();
                BigDecimal subTotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
                total = total.add(subTotal);

                DetalleVenta detalle = new DetalleVenta();
                detalle.setDetalleproducto(producto);
                detalle.setCantidad(cantidad);
                detalle.setPrecioUnitario(precioUnitario);
                detalle.setSubTotal(subTotal);
                detalles.add(detalle);
            }

            if (detalles.isEmpty()) {
                model.addAttribute("error", "No se encontraron productos válidos. Verifique la selección.");
                cargarDatosFormulario(model);
                return "venta/formulario";
            }

            // Guardar la venta primero
            Venta venta = new Venta();
            venta.setClienteVenta(cliente);
            venta.setUsuarioVenta(usuario);
            venta.setTotal(total);
            venta.setEstado(estado);
            venta.setFechaVenta(LocalDate.parse(fechaVenta));
            Venta ventaGuardada = ventaService.guardar(venta);

            // Guardar cada detalle asociado a la venta
            for (DetalleVenta detalle : detalles) {
                detalle.setDetalleVenta(ventaGuardada);
                detalleVentaService.guardar(detalle);
            }

            return "redirect:/venta/lista";

        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar: " + e.getMessage());
            cargarDatosFormulario(model);
            return "venta/formulario";
        }
    }
    // Ver detalle
    @GetMapping("/ver/{codigoVenta}")
    public String verDetalle(@PathVariable(name = "codigoVenta") Long codigoVenta, Model model) {
        Venta venta = ventaService.buscarPorCodigoVenta(codigoVenta).orElse(null);
        model.addAttribute("venta", venta);
        if (venta == null) model.addAttribute("error", "Venta no encontrada");
        return "venta/detalle";
    }

    // Editar (opcional) – si no lo necesitas, elimina este método y la vista de edición
    @GetMapping("/editar/{codigoVenta}")
    public String mostrarFormularioEditar(@PathVariable(name = "codigoVenta") Long codigoVenta, Model model) {
        Venta venta = ventaService.buscarPorCodigoVenta(codigoVenta).orElse(null);
        if (venta == null) {
            model.addAttribute("error", "Venta no encontrada");
            return "redirect:/venta/lista";
        }
        model.addAttribute("venta", venta);
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("productos", productosService.listarTodos());
        return "venta/formulario";
    }

    // Actualizar (opcional)
    @PostMapping("/actualizar/{codigoVenta}")
    public String actualizarVenta(@PathVariable(name = "codigoVenta") Long codigoVenta,
                                  @RequestParam(name = "dpiCliente") String dpiCliente,
                                  @RequestParam(name = "idUsuario") Long idUsuario,
                                  @RequestParam(name = "total") BigDecimal total,
                                  @RequestParam(name = "fechaVenta") String fechaVenta,
                                  @RequestParam(name = "estado") Long estado,
                                  Model model) {
        try {
            Cliente cliente = clienteService.buscarPorDPI(dpiCliente).orElse(null);
            Usuario usuario = usuarioService.findById(idUsuario).orElse(null);
            if (cliente == null || usuario == null) {
                model.addAttribute("error", "Cliente o Usuario no válido");
                cargarDatosFormulario(model);
                return "venta/formulario";
            }
            Venta venta = ventaService.buscarPorCodigoVenta(codigoVenta).orElse(null);
            if (venta == null) {
                model.addAttribute("error", "Venta no encontrada");
                return "redirect:/venta/lista";
            }
            venta.setClienteVenta(cliente);
            venta.setUsuarioVenta(usuario);
            venta.setTotal(total);
            venta.setEstado(estado);
            venta.setFechaVenta(LocalDate.parse(fechaVenta));
            ventaService.actualizar(codigoVenta, venta);
            return "redirect:/venta/lista";
        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar: " + e.getMessage());
            cargarDatosFormulario(model);
            return "venta/formulario";
        }
    }

    // Eliminar
    @GetMapping("/eliminar/{codigoVenta}")
    public String eliminarVenta(@PathVariable(name = "codigoVenta") Long codigoVenta) {
        ventaService.eliminar(codigoVenta);
        return "redirect:/venta/lista";
    }

    private void cargarDatosFormulario(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("productos", productosService.listarTodos());
    }
}