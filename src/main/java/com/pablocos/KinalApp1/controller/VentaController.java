package com.pablocos.KinalApp1.controller;

import com.pablocos.KinalApp1.entity.Cliente;
import com.pablocos.KinalApp1.entity.Usuario;
import com.pablocos.KinalApp1.entity.Venta;
import com.pablocos.KinalApp1.service.IClienteService;
import com.pablocos.KinalApp1.service.IUsuarioService;
import com.pablocos.KinalApp1.service.IVentaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/venta")
public class VentaController {

    private final IVentaService ventaService;
    private final IClienteService clienteService;
    private final IUsuarioService usuarioService;

    public VentaController(IVentaService ventaService,
                           IClienteService clienteService,
                           IUsuarioService usuarioService) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.usuarioService = usuarioService;
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
                               @RequestParam(name = "total") BigDecimal total,
                               @RequestParam(name = "fechaVenta") String fechaVenta,
                               @RequestParam(name = "estado") Long estado,
                               Model model) {
        System.out.println("DPI recibido: '" + dpiCliente + "'");
        System.out.println("ID Usuario recibido: " + idUsuario);
        try {
            String dpiLimpio = dpiCliente.trim();
            Cliente cliente = clienteService.buscarPorDPI(dpiLimpio).orElse(null);
            Usuario usuario = (idUsuario != null) ? usuarioService.findById(idUsuario).orElse(null) : null;

            if (cliente == null) {
                System.out.println("Cliente NO encontrado con DPI: " + dpiLimpio);
                model.addAttribute("error", "Cliente no válido (DPI: " + dpiLimpio + ")");
                cargarDatosFormulario(model);
                return "venta/formulario";
            }
            if (usuario == null) {
                System.out.println("Usuario NO encontrado con ID: " + idUsuario);
                model.addAttribute("error", "Usuario no válido (ID: " + idUsuario + ")");
                cargarDatosFormulario(model);
                return "venta/formulario";
            }

            Venta venta = new Venta();
            venta.setClienteVenta(cliente);
            venta.setUsuarioVenta(usuario);
            venta.setTotal(total);
            venta.setEstado(estado);
            venta.setFechaVenta(LocalDate.parse(fechaVenta));
            ventaService.guardar(venta);
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
                return "venta/formulario";
            }
            Venta venta = ventaService.buscarPorCodigoVenta(codigoVenta).orElse(null);
            if (venta == null) {
                model.addAttribute("error", "Venta no encontrada");
                return "venta/formulario";
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
    }
}