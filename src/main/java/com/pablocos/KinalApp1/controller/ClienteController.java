package com.pablocos.KinalApp1.controller;

import com.pablocos.KinalApp1.entity.Cliente;
import com.pablocos.KinalApp1.service.IClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final IClienteService clienteService;

    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/lista")
    public String listarVista(@RequestParam(name = "buscar", required = false) String buscar, Model model) {
        List<Cliente> clientes;

        if (buscar != null && !buscar.trim().isEmpty()) {
            var clienteOptional = clienteService.buscarPorDPI(buscar);

            if (clienteOptional.isPresent()) {
                clientes = List.of(clienteOptional.get());
                model.addAttribute("buscar", buscar);
            } else {
                clientes = clienteService.listarTodos().stream()
                        .filter(c -> c.getNombreCliente().toLowerCase().contains(buscar.toLowerCase()) ||
                                c.getApellidoCliente().toLowerCase().contains(buscar.toLowerCase()))
                        .toList();

                if (!clientes.isEmpty()) {
                    model.addAttribute("buscar", buscar);
                } else {
                    model.addAttribute("error", "No se encontraron clientes con: " + buscar);
                    clientes = clienteService.listarTodos();
                }
            }
        } else {
            clientes = clienteService.listarTodos();
        }

        model.addAttribute("clientes", clientes);
        return "clientes/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/formulario";
    }

    @PostMapping("/guardar")
    public String guardarCliente(@ModelAttribute Cliente cliente, Model model) {
        try {
            clienteService.guardar(cliente);
            return "redirect:/clientes/lista";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("cliente", cliente);
            return "clientes/formulario";
        }
    }

    @GetMapping("/ver/{dpi}")
    public String verDetalle(@PathVariable(name = "dpi") String dpi, Model model) {
        clienteService.buscarPorDPI(dpi)
                .ifPresentOrElse(
                        cliente -> model.addAttribute("cliente", cliente),
                        () -> model.addAttribute("error", "Cliente no encontrado")
                );
        return "clientes/detalle";
    }

    @GetMapping("/editar/{dpi}")
    public String mostrarFormularioEditar(@PathVariable(name = "dpi") String dpi, Model model) {
        clienteService.buscarPorDPI(dpi)
                .ifPresentOrElse(
                        cliente -> model.addAttribute("cliente", cliente),
                        () -> model.addAttribute("error", "Cliente no encontrado")
                );
        return "clientes/formulario";
    }

    @PostMapping("/actualizar/{dpi}")
    public String actualizarCliente(@PathVariable(name = "dpi") String dpi,
                                    @ModelAttribute Cliente cliente,
                                    Model model) {
        try {
            clienteService.actualizar(dpi, cliente);
            return "redirect:/clientes/lista";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Cliente no encontrado");
            return "clientes/formulario";
        }
    }

    @GetMapping("/eliminar/{dpi}")
    public String eliminarCliente(@PathVariable(name = "dpi") String dpi) {
        if (clienteService.existePorDPI(dpi)) {
            clienteService.eliminar(dpi);
        }
        return "redirect:/clientes/lista";
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listar() {
        List<Cliente> clientes = clienteService.listarTodos();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/api/{dpi}")
    public ResponseEntity<Cliente> buscarDPI(@PathVariable(name = "dpi") String dpi) {
        return clienteService.buscarPorDPI(dpi)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Cliente cliente) {
        try {
            Cliente nuevoCliente = clienteService.guardar(cliente);
            return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{dpi}")
    public ResponseEntity<Void> eliminar(@PathVariable(name = "dpi") String dpi) {
        try {
            if (!clienteService.existePorDPI(dpi)) {
                return ResponseEntity.notFound().build();
            }
            clienteService.eliminar(dpi);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{dpi}")
    public ResponseEntity<?> actualizar(@PathVariable(name = "dpi") String dpi, @RequestBody Cliente cliente) {
        try {
            if (!clienteService.existePorDPI(dpi)) {
                return ResponseEntity.notFound().build();
            }
            Cliente clienteActualizado = clienteService.actualizar(dpi, cliente);
            return ResponseEntity.ok(clienteActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Cliente>> buscarPorEstado(@PathVariable(name = "estado") Long estado) {
        List<Cliente> clientes = clienteService.buscarPorEstadoConFor(estado);
        if (clientes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(clientes);
    }
}