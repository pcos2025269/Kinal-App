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

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    private final IVentaService ventaService;
    private final IClienteService clienteService;
    private final IUsuarioService usuarioService;

    public VentaController(IVentaService ventaService, IClienteService clienteService, IUsuarioService usuarioService) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.usuarioService = usuarioService;
    }

    // VISTAS
    @GetMapping("/lista")
    public String listarVista(@RequestParam(required = false) String buscar, Model model) {
        List<Venta> ventas;

        if (buscar != null && !buscar.trim().isEmpty()) {
            try {
                Long codigo = Long.parseLong(buscar);
                var ventaOptional = ventaService.buscarPorCodigoVenta(codigo);

                if (ventaOptional.isPresent()) {
                    ventas = List.of(ventaOptional.get());
                    model.addAttribute("buscar", buscar);
                } else {
                    ventas = ventaService.listarTodos().stream()
                            .filter(v -> v.getClienteVenta().getDPICliente().contains(buscar) ||
                                    v.getUsuarioVenta().getNameUser().toLowerCase().contains(buscar.toLowerCase()))
                            .toList();

                    if (!ventas.isEmpty()) {
                        model.addAttribute("buscar", buscar);
                    } else {
                        model.addAttribute("error", "No se encontraron ventas con: " + buscar);
                        ventas = ventaService.listarTodos();
                    }
                }
            } catch (NumberFormatException e) {
                ventas = ventaService.listarTodos().stream()
                        .filter(v -> v.getClienteVenta().getDPICliente().contains(buscar) ||
                                v.getUsuarioVenta().getNameUser().toLowerCase().contains(buscar.toLowerCase()))
                        .toList();

                if (!ventas.isEmpty()) {
                    model.addAttribute("buscar", buscar);
                } else {
                    model.addAttribute("error", "No se encontraron ventas con: " + buscar);
                    ventas = ventaService.listarTodos();
                }
            }
        } else {
            ventas = ventaService.listarTodos();
        }

        model.addAttribute("ventas", ventas);
        return "ventas/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("venta", new Venta());
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "ventas/formulario";
    }

    @PostMapping("/guardar")
    public String guardarVenta(@ModelAttribute Venta venta,
                               @RequestParam String dpiCliente,
                               @RequestParam Long codigoUsuario,
                               Model model) {
        try {
            Cliente cliente = clienteService.buscarPorDPI(dpiCliente).orElse(null);
            Usuario usuario = usuarioService.findByCodigoUsuario(codigoUsuario).orElse(null);

            venta.setClienteVenta(cliente);
            venta.setUsuarioVenta(usuario);
            venta.setFechaVenta(LocalDate.now());

            ventaService.guardar(venta);
            return "redirect:/ventas/lista";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("venta", venta);
            model.addAttribute("clientes", clienteService.listarTodos());
            model.addAttribute("usuarios", usuarioService.listarTodos());
            return "ventas/formulario";
        }
    }

    @GetMapping("/ver/{codigoVenta}")
    public String verDetalle(@PathVariable Long codigoVenta, Model model) {
        ventaService.buscarPorCodigoVenta(codigoVenta)
                .ifPresentOrElse(
                        venta -> model.addAttribute("venta", venta),
                        () -> model.addAttribute("error", "Venta no encontrada")
                );
        return "ventas/detalle";
    }

    @GetMapping("/editar/{codigoVenta}")
    public String mostrarFormularioEditar(@PathVariable Long codigoVenta, Model model) {
        ventaService.buscarPorCodigoVenta(codigoVenta)
                .ifPresentOrElse(
                        venta -> {
                            model.addAttribute("venta", venta);
                            model.addAttribute("clientes", clienteService.listarTodos());
                            model.addAttribute("usuarios", usuarioService.listarTodos());
                        },
                        () -> model.addAttribute("error", "Venta no encontrada")
                );
        return "ventas/formulario";
    }

    @PostMapping("/actualizar/{codigoVenta}")
    public String actualizarVenta(@PathVariable Long codigoVenta,
                                  @ModelAttribute Venta venta,
                                  @RequestParam String dpiCliente,
                                  @RequestParam Long codigoUsuario,
                                  Model model) {
        try {
            Cliente cliente = clienteService.buscarPorDPI(dpiCliente).orElse(null);
            Usuario usuario = usuarioService.findByCodigoUsuario(codigoUsuario).orElse(null);

            venta.setCodigoVenta(codigoVenta);
            venta.setClienteVenta(cliente);
            venta.setUsuarioVenta(usuario);

            ventaService.actualizar(codigoVenta, venta);
            return "redirect:/ventas/lista";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Venta no encontrada");
            return "ventas/formulario";
        }
    }

    @GetMapping("/eliminar/{codigoVenta}")
    public String eliminarVenta(@PathVariable Long codigoVenta) {
        if (ventaService.existePorCodigoVenta(codigoVenta)) {
            ventaService.eliminar(codigoVenta);
        }
        return "redirect:/ventas/lista";
    }

    // API REST
    @GetMapping("/api")
    public ResponseEntity<List<Venta>> listar() {
        List<Venta> ventas = ventaService.listarTodos();
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/api/{codigoVenta}")
    public ResponseEntity<Venta> buscarId(@PathVariable Long codigoVenta) {
        return ventaService.buscarPorCodigoVenta(codigoVenta)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api")
    public ResponseEntity<?> guardar(@RequestBody Venta venta) {
        try {
            Venta nuevaVenta = ventaService.guardar(venta);
            return new ResponseEntity<>(nuevaVenta, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/api/{codigoVenta}")
    public ResponseEntity<Void> eliminar(@PathVariable Long codigoVenta) {
        try {
            if (!ventaService.existePorCodigoVenta(codigoVenta)) {
                return ResponseEntity.notFound().build();
            }
            ventaService.eliminar(codigoVenta);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/api/{codigoVenta}")
    public ResponseEntity<?> actualizar(@PathVariable Long codigoVenta, @RequestBody Venta venta) {
        try {
            if (!ventaService.existePorCodigoVenta(codigoVenta)) {
                return ResponseEntity.notFound().build();
            }
            Venta ventaActualizada = ventaService.actualizar(codigoVenta, venta);
            return ResponseEntity.ok(ventaActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/estado/{estado}")
    public ResponseEntity<List<Venta>> buscarPorEstado(@PathVariable Long estado) {
        List<Venta> venta = ventaService.buscarPorEstadoConFor(estado);
        if (venta.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(venta);
    }
}