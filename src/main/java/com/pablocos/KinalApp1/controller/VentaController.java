package com.pablocos.KinalApp1.controller;

import com.pablocos.KinalApp1.entity.Cliente;
import com.pablocos.KinalApp1.entity.Usuario;
import com.pablocos.KinalApp1.entity.Venta;
import com.pablocos.KinalApp1.service.IClienteService;
import com.pablocos.KinalApp1.service.IUsuarioService;
import com.pablocos.KinalApp1.service.IVentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public VentaController(IVentaService ventaService, IClienteService clienteService, IUsuarioService usuarioService) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/lista")
    public String listarVista(@RequestParam(required = false) String buscar, Model model) {
        List<Venta> ventas = ventaService.listarTodos();
        model.addAttribute("ventas", ventas);
        return "venta/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("venta", new Venta());
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "venta/formulario";
    }

    @PostMapping("/guardar")
    public String guardarVenta(@RequestParam String dpiCliente,
                               @RequestParam Long id,
                               @RequestParam BigDecimal total,
                               Model model) {
        try {
            Cliente cliente = clienteService.buscarPorDPI(dpiCliente).orElse(null);
            Usuario usuario = usuarioService.findById(id).orElse(null);

            Venta venta = new Venta();
            venta.setClienteVenta(cliente);
            venta.setUsuarioVenta(usuario);
            venta.setTotal(total);
            venta.setEstado(1L);
            venta.setFechaVenta(LocalDate.now());

            ventaService.guardar(venta);
            return "redirect:/venta/lista";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("clientes", clienteService.listarTodos());
            model.addAttribute("usuarios", usuarioService.listarTodos());
            return "venta/formulario";
        }
    }

    @GetMapping("/ver/{codigoVenta}")
    public String verDetalle(@PathVariable Long codigoVenta, Model model) {
        Venta venta = ventaService.buscarPorCodigoVenta(codigoVenta).orElse(null);
        model.addAttribute("venta", venta);
        return "venta/detalle";
    }

    @GetMapping("/editar/{codigoVenta}")
    public String mostrarFormularioEditar(@PathVariable Long codigoVenta, Model model) {
        Venta venta = ventaService.buscarPorCodigoVenta(codigoVenta).orElse(null);
        model.addAttribute("venta", venta);
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "venta/formulario";
    }

    @PostMapping("/actualizar/{codigoVenta}")
    public String actualizarVenta(@PathVariable Long codigoVenta,
                                  @RequestParam String dpiCliente,
                                  @RequestParam Long id,
                                  @RequestParam BigDecimal total,
                                  Model model) {
        try {
            Cliente cliente = clienteService.buscarPorDPI(dpiCliente).orElse(null);
            Usuario usuario = usuarioService.findById(id).orElse(null);

            Venta venta = ventaService.buscarPorCodigoVenta(codigoVenta).orElse(null);
            venta.setClienteVenta(cliente);
            venta.setUsuarioVenta(usuario);
            venta.setTotal(total);
            venta.setFechaVenta(LocalDate.now());

            ventaService.actualizar(codigoVenta, venta);
            return "redirect:/venta/lista";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "venta/formulario";
        }
    }

    @GetMapping("/eliminar/{codigoVenta}")
    public String eliminarVenta(@PathVariable Long codigoVenta) {
        ventaService.eliminar(codigoVenta);
        return "redirect:/venta/lista";
    }
}